package com.wetech.demo.web3j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class TransferFromRequest {
    private String from;
    private String to;
    private BigInteger value;
}
