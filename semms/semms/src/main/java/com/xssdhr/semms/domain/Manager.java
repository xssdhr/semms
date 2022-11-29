package com.xssdhr.semms.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager implements Serializable {

    private static final long serialVersionUID = -40356785423868312L;
    private String man_id;
    private String man_hash_id;
    private String hos_id;
    private String hos_hash_id;
    private String name;
    private String phone;
    private String email;
    private String login_id;
    private String password;
    private String status;
}
