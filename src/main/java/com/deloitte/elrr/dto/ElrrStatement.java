/**
 *
 */
package com.deloitte.elrr.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author mnelakurti
 *
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ElrrStatement implements Serializable {

    /**
     *
     */
    @Generated
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @Generated
    private String actor;
    /**
    *
    */
    @Generated
    private String actorName;
    /**
    *
    */
    @Generated
    private String verb;
    /**
    *
    */
    @Generated
    private String activity;
    /**
    *
    */
    @Generated
    private String courseName;
    /**
    *
    */
    @Generated
    private String statementjson;
}
