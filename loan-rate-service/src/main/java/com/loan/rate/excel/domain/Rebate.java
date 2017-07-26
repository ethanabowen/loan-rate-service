package com.loan.rate.excel.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Rebate {

	BigDecimal rebate;
	LockDate lockDate;
	
}