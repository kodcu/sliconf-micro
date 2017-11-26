package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.Sponsor;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SponsorSpecs {

    private SponsorSpecs() {
        // Static yapi icin gizli yapici yordam
    }

    /**
     * Gelen sponsor map in gecerli olup olmadigini kontrol eder <br/>
     * <p>
     * SponsorMap:
     * Key -> sponsorTagId belirtiliyor , Value sponsor listesi
     * <p>
     * SponsorTags:
     * Key -> sponsorIg , Value tagName
     *
     * @param sponsorMap
     * @param sponsorTags
     * @return
     */
    public static ResponseMessage isSponsorMapValid(Map<String, List<Sponsor>> sponsorMap, Map<String, String> sponsorTags) {
        ResponseMessage responseMessage;

        responseMessage = isSponsorTagsValid(sponsorTags);

        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        for (Map.Entry<String, List<Sponsor>> entry : sponsorMap.entrySet()) {

            responseMessage = isSponsorTagValid(entry.getKey(), sponsorTags);

            if (!responseMessage.isStatus()) {
                return responseMessage;
            }

            responseMessage = isSponsorListValid(entry.getValue());

            if (!responseMessage.isStatus()) {
                return responseMessage;
            }
        }

        responseMessage.setStatus(true);
        responseMessage.setMessage("SponsorMap and sponsorTags are valid");
        responseMessage.setReturnObject("");

        return responseMessage;
    }

    /**
     * Sponsor listesi icerisindeki verinin gecerli olup olmadigi kontrol ediliyor
     *
     * @param sponsors
     * @return
     */
    private static ResponseMessage isSponsorListValid(List<Sponsor> sponsors) {
        ResponseMessage responseMessage = new ResponseMessage(true, "", "");

        if (Objects.nonNull(sponsors)) {
            for (Sponsor sponsor : sponsors) {
                if (Objects.isNull(sponsor) ||
                        Objects.isNull(sponsor.getLogo()) || sponsor.getLogo().isEmpty() ||
                        Objects.isNull(sponsor.getName()) || sponsor.getName().isEmpty()) {
                    responseMessage.setStatus(false);
                    responseMessage.setMessage("Sponsor data must be filled, can not be empty!");
                    responseMessage.setReturnObject(sponsors);
                    break;
                }

                // generateIdIfNeeded(sponsor);
            }
        }

        return responseMessage;
    }

    /**
     * Gelen sponsor nesnesi null degilse ve id'si null ya da bos ise yeni bir id uretir
     *
     * @param sponsor
     */
    private static void generateIdIfNeeded(Sponsor sponsor) {
        if (Objects.nonNull(sponsor) &&
                (Objects.isNull(sponsor.getId()) || "".equals(sponsor.getId()))) {
            sponsor.setId(UUID.randomUUID().toString());
        }
    }

    /**
     * Gelen sponsor taglarinin gecerli olup olmadigina bakar
     *
     * @param sponsorTagsMap
     * @return
     */
    private static ResponseMessage isSponsorTagsValid(Map<String, String> sponsorTagsMap) {
        ResponseMessage responseMessage = new ResponseMessage(true, "Sponsor tags are valid", "");

        for (Map.Entry<String, String> entry : sponsorTagsMap.entrySet()) {
            if (Objects.isNull(entry.getKey()) || Objects.isNull(entry.getValue())) {
                responseMessage.setStatus(false);
                responseMessage.setMessage("Sponsor tags can not be empty!");
                responseMessage.setReturnObject(sponsorTagsMap);
                break;
            }
        }

        return responseMessage;
    }

    /**
     * Verilen sponsorTag'in sponsorTagsMap de blunup bulunmadigini kontrol eder.
     * Boylece olmayan taglere sponsor eklenmesi onlenir
     *
     * @param sponsorTag
     * @param sponsorTagsMap
     * @return
     */
    private static ResponseMessage isSponsorTagValid(String sponsorTag, Map<String, String> sponsorTagsMap) {
        ResponseMessage responseMessage = new ResponseMessage(true, "", "");

        if (Objects.isNull(sponsorTag) || Objects.isNull(sponsorTagsMap) || !sponsorTagsMap.containsKey(sponsorTag)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage("Sponsor tag is not in the sponsorTags map");
            responseMessage.setReturnObject("");
        }

        return responseMessage;
    }
}
