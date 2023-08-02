package cl.saraos.bank.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    private Long number;
    private int citycode;
    private String contrycode;
}
