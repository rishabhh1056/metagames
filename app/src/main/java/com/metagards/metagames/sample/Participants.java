
package com.metagards.metagames.sample;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("jsonschema2pojo")
public class Participants {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("table_data")
    @Expose
    private List<TableDatum> tableData = null;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TableDatum> getTableData() {
        return tableData;
    }

    public void setTableData(List<TableDatum> tableData) {
        this.tableData = tableData;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
