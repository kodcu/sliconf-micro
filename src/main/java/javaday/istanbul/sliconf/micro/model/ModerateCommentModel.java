package javaday.istanbul.sliconf.micro.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Web uzerinde gelen yorumlarin onaylanip ya da onaylanmadigini gosteren model nesnesi
 * approved ve denied listesi icerisinde degerler commentlerin id sidir
 */
@Getter
@Setter
public class ModerateCommentModel {
    private String eventId;
    private String userId;
    private List<String> approved;
    private List<String> denied;

}
