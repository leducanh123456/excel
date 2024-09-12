package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class TestDto {

    private String sapCode;

    private String projectName;

    private BigDecimal budget;

    private Date startDate;

    private Date endDate;

    private Integer total;

    private String code;

    //Policy

    private Date startDatePolicy;

    private Date endDatePolicy;

    private Integer budgetPolicy;

    private String codePolicy;

    private LocalDate startDateApproval;

    private LocalDate endDateApproval;

    private BigDecimal totalApproval;

    private Float codeApproval;

    private String sapCode1;

    private String projectName1;

    private BigDecimal budget1;

    private Date startDate1;

    private Date endDate1;

    private Integer total1;

    private String code1;

    //Policy

    private Date startDatePolicy1;

    private Date endDatePolicy1;

    private Integer budgetPolicy1;

    private String codePolicy1;

    private LocalDate startDateApproval1;

    private LocalDate endDateApproval1;

    private BigDecimal totalApproval1;

    private Float codeApproval1;

    private String sapCode2;

    private String projectName2;

    private BigDecimal budget2;

    private Date startDate2;

    private Date endDate2;

    private Integer total2;

    private String code2;

    //Policy

    private Date startDatePolicy2;

    private Date endDatePolicy2;

    private Integer budgetPolicy2;

    private String codePolicy2;

    private LocalDate startDateApproval2;

    private LocalDate endDateApproval2;

    private BigDecimal totalApproval2;

    private Float codeApproval2;

    private String sapCode3;

    private String projectName3;

    private BigDecimal budget3;

    private Date startDate3;

    private Date endDate3;

    private Integer total3;

    private String code3;

    //Policy

    private Date startDatePolicy3;

    private Date endDatePolicy3;

    private Integer budgetPolicy3;

    private String codePolicy3;

    private LocalDate startDateApproval3;

    private LocalDate endDateApproval3;

    private BigDecimal totalApproval3;

    private Float codeApproval3;

    private String sapCode4;

    private String projectName4;

    private BigDecimal budget4;

    private Date startDate4;

    private Date endDate4;

    private Integer total4;

    private String code4;

    //Policy

    private Date startDatePolicy4;

    private Date endDatePolicy4;

    private Integer budgetPolicy4;

    private String codePolicy4;

    private LocalDate startDateApproval4;

    private LocalDate endDateApproval4;

    private BigDecimal totalApproval4;

    private Float codeApproval4;

    private String sapCode5;

    private String projectName5;

    private BigDecimal budget5;

    private Date startDate5;

    private Date endDate5;

    private Integer total5;

    private String code5;

    //Policy

    private Date startDatePolicy5;

    private Date endDatePolicy5;

    private Integer budgetPolicy5;

    private String codePolicy5;

    private LocalDate startDateApproval5;

    private LocalDate endDateApproval5;

    private BigDecimal totalApproval5;

    private Float codeApproval5;

    @Override
    public String toString() {
        return "TestDto{" +
                getBasicFields() +
                getPolicyFields() +
                getFields1() +
                getPolicyFields1() +
                getFields2() +
                getPolicyFields2() +
                getFields3() +
                getPolicyFields3() +
                getFields4() +
                getPolicyFields4() +
                getFields5() +
                getPolicyFields5() +
                '}';
    }

    private String getBasicFields() {
        return "sapCode='" + sapCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", budget=" + budget +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", total=" + total +
                ", code='" + code + '\'';
    }

    private String getPolicyFields() {
        return ", startDatePolicy=" + startDatePolicy +
                ", endDatePolicy=" + endDatePolicy +
                ", budgetPolicy=" + budgetPolicy +
                ", codePolicy='" + codePolicy + '\'' +
                ", startDateApproval=" + startDateApproval +
                ", endDateApproval=" + endDateApproval +
                ", totalApproval=" + totalApproval +
                ", codeApproval=" + codeApproval;
    }

    private String getFields1() {
        return ", sapCode1='" + sapCode1 + '\'' +
                ", projectName1='" + projectName1 + '\'' +
                ", budget1=" + budget1 +
                ", startDate1=" + startDate1 +
                ", endDate1=" + endDate1 +
                ", total1=" + total1 +
                ", code1='" + code1 + '\'';
    }

    private String getPolicyFields1() {
        return ", startDatePolicy1=" + startDatePolicy1 +
                ", endDatePolicy1=" + endDatePolicy1 +
                ", budgetPolicy1=" + budgetPolicy1 +
                ", codePolicy1='" + codePolicy1 + '\'' +
                ", startDateApproval1=" + startDateApproval1 +
                ", endDateApproval1=" + endDateApproval1 +
                ", totalApproval1=" + totalApproval1 +
                ", codeApproval1=" + codeApproval1;
    }

    private String getFields2() {
        return ", sapCode2='" + sapCode2 + '\'' +
                ", projectName2='" + projectName2 + '\'' +
                ", budget2=" + budget2 +
                ", startDate2=" + startDate2 +
                ", endDate2=" + endDate2 +
                ", total2=" + total2 +
                ", code2='" + code2 + '\'';
    }

    private String getPolicyFields2() {
        return ", startDatePolicy2=" + startDatePolicy2 +
                ", endDatePolicy2=" + endDatePolicy2 +
                ", budgetPolicy2=" + budgetPolicy2 +
                ", codePolicy2='" + codePolicy2 + '\'' +
                ", startDateApproval2=" + startDateApproval2 +
                ", endDateApproval2=" + endDateApproval2 +
                ", totalApproval2=" + totalApproval2 +
                ", codeApproval2=" + codeApproval2;
    }

    private String getFields3() {
        return ", sapCode3='" + sapCode3 + '\'' +
                ", projectName3='" + projectName3 + '\'' +
                ", budget3=" + budget3 +
                ", startDate3=" + startDate3 +
                ", endDate3=" + endDate3 +
                ", total3=" + total3 +
                ", code3='" + code3 + '\'';
    }

    private String getPolicyFields3() {
        return ", startDatePolicy3=" + startDatePolicy3 +
                ", endDatePolicy3=" + endDatePolicy3 +
                ", budgetPolicy3=" + budgetPolicy3 +
                ", codePolicy3='" + codePolicy3 + '\'' +
                ", startDateApproval3=" + startDateApproval3 +
                ", endDateApproval3=" + endDateApproval3 +
                ", totalApproval3=" + totalApproval3 +
                ", codeApproval3=" + codeApproval3;
    }

    private String getFields4() {
        return ", sapCode4='" + sapCode4 + '\'' +
                ", projectName4='" + projectName4 + '\'' +
                ", budget4=" + budget4 +
                ", startDate4=" + startDate4 +
                ", endDate4=" + endDate4 +
                ", total4=" + total4 +
                ", code4='" + code4 + '\'';
    }

    private String getPolicyFields4() {
        return ", startDatePolicy4=" + startDatePolicy4 +
                ", endDatePolicy4=" + endDatePolicy4 +
                ", budgetPolicy4=" + budgetPolicy4 +
                ", codePolicy4='" + codePolicy4 + '\'' +
                ", startDateApproval4=" + startDateApproval4 +
                ", endDateApproval4=" + endDateApproval4 +
                ", totalApproval4=" + totalApproval4 +
                ", codeApproval4=" + codeApproval4;
    }

    private String getFields5() {
        return ", sapCode5='" + sapCode5 + '\'' +
                ", projectName5='" + projectName5 + '\'' +
                ", budget5=" + budget5 +
                ", startDate5=" + startDate5 +
                ", endDate5=" + endDate5 +
                ", total5=" + total5 +
                ", code5='" + code5 + '\'';
    }

    private String getPolicyFields5() {
        return ", startDatePolicy5=" + startDatePolicy5 +
                ", endDatePolicy5=" + endDatePolicy5 +
                ", budgetPolicy5=" + budgetPolicy5 +
                ", codePolicy5='" + codePolicy5 + '\'' +
                ", startDateApproval5=" + startDateApproval5 +
                ", endDateApproval5=" + endDateApproval5 +
                ", totalApproval5=" + totalApproval5 +
                ", codeApproval5=" + codeApproval5;
    }

}
