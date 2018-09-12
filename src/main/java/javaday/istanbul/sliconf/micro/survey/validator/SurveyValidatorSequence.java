package javaday.istanbul.sliconf.micro.survey.validator;


import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyQuestionOptionValidatorGroup;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyQuestionValidatorGroup;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyStartAndEndLocalDateTimeValidatorGroup;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyValidatorGroup;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;
/**
 *  {@link Survey} modelini validate ederken  hangi kisitlamalarin önce calisacagini belirlemek icin kullanilir.
 *  Burada önce ana Survey modeli, daha sonra sınıf seviyesinde olan başlangıç ve bitiş tarihi kontrol ediliyor.
 *  Bunun sebebi default olarak sınıf seviyesinde uygulanan {@link ValidStartAndEndLocalDateTime} kısıtlaması
 *  tarih verisini kontrol ettiğimiz patternden den önce çalışacağı için  yanlış girilen bir LocalDateTime validate edilmeden
 *  başlangıç ve bitiş zamanı kullanılmaya çalışılacak. Eğer başlangıç ve bitiş tarihi uygun formatta değil ise
 *  bunlardan {@link java.time.LocalDateTime} oluşturulamayacağı için  {@link NumberFormatException} türünde
 *  bir exceptiona sebep olacak. Bunun önüne geçmek için bu yapıyla önce doğru bir {@link java.time.LocalDateTime}
 *  verisi aldığımızı doğruluyoruz.
 */


@GroupSequence({
        Default.class,
        SurveyValidatorGroup.class,
        SurveyStartAndEndLocalDateTimeValidatorGroup.class,
        SurveyQuestionValidatorGroup.class,
        SurveyQuestionOptionValidatorGroup.class,
})

public interface SurveyValidatorSequence {
}
