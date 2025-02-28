package com.example.stockaccounts.service;

import com.example.stockaccounts.dto.MagicFormulaDTO;
import com.example.stockaccounts.entity.MagicFormulaStock;
import com.example.stockaccounts.mapper.MagicFormulaMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MagicFormulaService {
    private static final String FUNDAMENTUS_URL = "https://www.fundamentus.com.br/resultado.php";
    private static final BigDecimal MIN_LIQUIDITY_2_MONTHS = new BigDecimal("100000");
    private static final BigDecimal MIN_EV_EBIT = BigDecimal.ZERO;
    private static final List<String> EXCLUDED_SECTOR = List.of("20", "14", "31");

    private final MagicFormulaMapper mapper;

    public MagicFormulaService(MagicFormulaMapper mapper) {
        this.mapper = mapper;
    }

    public List<MagicFormulaStock> getMagicFormulaStocks() throws IOException {
        // Extrai dados e converte para modelo
        List<MagicFormulaStock> stockList = extract();
        // Aplica filtros
        stockList = applyFilters(stockList);
        // Aplica rankings
        return applyRanking(stockList);
    }

    private List<MagicFormulaStock> extract() throws IOException {
        Document document = Jsoup.connect(FUNDAMENTUS_URL).get();
        var stockList = documentExtract(document);
        return stockList
                .stream()
                .map(mapper::convertToModel)
                .collect(Collectors.toList());
    }

    private Set<String> extractToExclude(String sector) throws IOException {
        Document document = Jsoup.connect(FUNDAMENTUS_URL.concat("?setor="+sector)).get();
        var stockList = documentExtract(document);
        return stockList.stream().map(MagicFormulaDTO::papel).collect(Collectors.toSet());
    }

    private List<MagicFormulaDTO> documentExtract(Document document){
        Element table = document.select("table#resultado").first();

        boolean header = true;
        List<MagicFormulaDTO> stockList = new ArrayList<>();
        for(Element row : table.select("tr")) {
            if(header) {
                header = false;
                continue;
            }
            Elements column = row.select("td");
            MagicFormulaDTO magicFormulaDTO = new MagicFormulaDTO(
                    column.get(0).select("a").text(),
                    column.get(1).text(),
                    column.get(2).text(),
                    column.get(10).text(),
                    column.get(15).text(),
                    column.get(17).text()
            );
            stockList.add(magicFormulaDTO);
        }
        return stockList;
    }

    private List<MagicFormulaStock> applyFilters(List<MagicFormulaStock> stockList) throws IOException {
        // Coleta ações a serem excluídas
        Set<String> exclusionStocks = collectExclusionStocks();
        // Aplica filtros
        return stockList
                .stream()
                .filter(stock -> stock.getLiq2meses().compareTo(MIN_LIQUIDITY_2_MONTHS) >= 0)
                .filter(stock -> stock.getEvEbit().compareTo(MIN_EV_EBIT) > 0)
                .filter(stock -> !exclusionStocks.contains(stock.getPapel()))
                .collect(Collectors.toMap(
                        stock -> stock.getPapel().replaceAll("[0-9]", ""),
                        Function.identity(),
                        (stock1, stock2) -> stock1.getLiq2meses().compareTo(stock2.getLiq2meses()) >= 0 ? stock1 : stock2
                ))
                .values()
                .stream()
                .toList();
    }

    private Set<String> collectExclusionStocks() throws IOException {
        Set<String> exclusionStocks = new HashSet<>();
        for (String sector : EXCLUDED_SECTOR){
            exclusionStocks.addAll(extractToExclude(sector));
        }
        return exclusionStocks;
    }

    private List<MagicFormulaStock> applyRanking(List<MagicFormulaStock> staticStockList){
        int size = staticStockList.size();
        List<MagicFormulaStock> stockList = new ArrayList<>(staticStockList);

        // Ranking por EV/EBIT (menor é melhor)
        AtomicInteger rankEvEbit = new AtomicInteger(1);
        stockList.sort(Comparator.comparing(MagicFormulaStock::getEvEbit));
        stockList.forEach(stock -> stock.setRankEvEbit(rankEvEbit.getAndIncrement()));

        // Ranking por ROIC (maior é melhor)
        AtomicInteger rankRoic = new AtomicInteger(1);
        stockList.sort(Comparator.comparing(MagicFormulaStock::getRoic).reversed());
        stockList.forEach(stock -> {
            stock.setRankRoic(rankRoic.getAndIncrement());
            stock.setRankGlobal(stock.getRankEvEbit() + stock.getRankRoic());
        });

        // Ranking global final
        AtomicInteger rankGlobal = new AtomicInteger(1);
        stockList.sort(Comparator.comparing(MagicFormulaStock::getRankGlobal));
        stockList.forEach(stock -> stock.setRankGlobal(rankGlobal.getAndIncrement()));

        return stockList;
    }
}
