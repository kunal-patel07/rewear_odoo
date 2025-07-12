package com.example.cafe_backedn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;

//    @NotNull
//    @NotBlank
//    private String bill_id;

    @NotNull
    @NotBlank
    private String customerName;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String number;

        @NotNull
        @NotBlank
        private String payment_option;

//        @NotNull
//        @NotBlank
//        private String product_category;

        @NotNull
        @NotBlank
        private String product_id;

        @NotNull
        @NotBlank
        private String quantity;

        @NotNull
        @NotBlank
        private String total;
}
