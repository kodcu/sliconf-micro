package javaday.istanbul.sliconf.micro.event.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Murat Özer
 * Etkinliğin olusturulma aşamasından sistemden kalıcı olarak silinme sürecine kadar ki durumlarını barındıran nesne.
 * Bu durumlar toplamda altı adet enum ile temsil edilmiştir. Şu an için bir etkinlik en fazla iki durumda bulunabilir.
 * Bunlardan biri her zaman {@link EventStatus#DELETED} durumudur.
 * Bunun dışında etkinliğin hangi durumda silindiğine bağlı olarak diğer durumlardan bir tanesini alabilir.
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class LifeCycleState implements Serializable {

    /**
     * Etkinliğin durumlarını tutan liste nesnesi.
     */
    private List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();

    public enum EventStatus {

        /**
         * Eventin açılıp gerekli bilgilerinin doldurulduğunu ve mobilden görülebilir olduğu anlamına gelir.
         */
        ACTIVE,

        /**
         * Eventin açılıp daha yeterli bilginin girilmediği anlamına gelir.
         */
        PASSIVE,

        /**
         * Etkinliğin şu anda gerçekleşiyor olduğunu belirtir.
         * Etkinlik {@link EventStatus#ACTIVE} iken başlarsa bu durumu alır.
         */
        HAPPENING,

        /**
         * Etkinliğin oluşturulup, başarılı bir şekilde gerçekleştikten sonra bittiği anlamına gelir.
         * Etkinlik {@link EventStatus#HAPPENING} iken biter ise bu duruma gelir.
         */
        FINISHED,

        /**
         * Etkinliğin etkinlik yöneticisi tarafından silindiği anlamına gelir.
         */
        DELETED,

        /**
         * Etkinlik oluşturulup gerekli bilgiler girilmeden başlarsa durumu alır.
         * Etkinlik {@link EventStatus#PASSIVE} iken başlangıç zamanı gelir ise bu duruma gelir.
         */
        FAILED
    }
}
