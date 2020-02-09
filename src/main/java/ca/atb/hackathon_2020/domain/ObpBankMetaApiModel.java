package ca.atb.hackathon_2020.domain;

import ca.atb.hackathon_2020.domain.ATM;
import ca.atb.hackathon_2020.domain.Bank;
import ca.atb.hackathon_2020.domain.Branch;
import lombok.Data;

import java.util.List;

public interface ObpBankMetaApiModel {
    @Data
    class Banks {
        private List<Bank> banks;
    }

    @Data
    class ATMs {
        private List<ATM> atms;
    }

    @Data
    class Branches {
        private List<Branch> branches;
    }

}
