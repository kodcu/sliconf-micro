package javaday.istanbul.test;

import javaday.istanbul.is.HisseSenet;
import cucumber.api.DataTable;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Ozaman;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class HisseSenediIslemleriSteps {


    private HisseSenet hisse;
    List<HisseSenet> hisseSenetListesi = new ArrayList<HisseSenet>();




    @Diyelimki("^ihbar tazminatının hesaplanması için gerekli çalışan bilgileri$")
    public void ihbarTazminatınınHesaplanmasıIçinGerekliÇalışanBilgileri(DataTable table) throws Throwable {



        for (Map<String, String> row : table.asMaps(String.class, String.class)) {

            HisseSenet hisseSenet = new HisseSenet();
            hisseSenet.setIsim(row.get("hisseler"));
            hisseSenet.setDeger(new BigDecimal(row.get("degerler")));

            hisseSenetListesi.add(hisseSenet);
        }



    }

    @Ozaman("^portfotyun değeri \"(.*?)\" olarak hesaplanır$")
    public void portfotyunDeğeriOlarakHesaplanır(String deger) throws Throwable {

        BigDecimal karsilastirilacakDeger = new BigDecimal(deger);

        BigDecimal toplananDegerlerinOrtalamasi = new BigDecimal(0);
        Iterator<HisseSenet> iterator = hisseSenetListesi.iterator();
        while (iterator.hasNext()) {
            HisseSenet hisseSenet = iterator.next();
            toplananDegerlerinOrtalamasi = toplananDegerlerinOrtalamasi.add(hisseSenet.getDeger());
        }

        toplananDegerlerinOrtalamasi = toplananDegerlerinOrtalamasi.divide(new BigDecimal(hisseSenetListesi.size()), 2, BigDecimal.ROUND_HALF_UP);

        assertEquals(toplananDegerlerinOrtalamasi, karsilastirilacakDeger);

    }



}