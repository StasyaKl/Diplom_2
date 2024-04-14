package site.nomoreparties.stellarburgers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetIngredientResponse {
    private boolean success;
    private List<Datas> data;
    private int total;
    private int totalToday;
}
