package ca.atb.hackathon_2020.domain;

import lombok.Data;

@Data
public class Branch {
    private String id;
    private String name;
    private Location location;
}
