package com.youlexuan.group;

import com.youlexuan.pojo.specification.Specification;
import com.youlexuan.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

public class SpecificationGroup implements Serializable {
    private Specification specification;
    private List<SpecificationOption> specificationOptionList;//specificationOptionList

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }

    @Override
    public String toString() {
        return "SpecificationGroup{" +
                "specification=" + specification +
                ", specificationOptionList=" + specificationOptionList +
                '}';
    }
}
