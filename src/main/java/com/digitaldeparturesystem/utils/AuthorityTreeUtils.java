package com.digitaldeparturesystem.utils;

import com.digitaldeparturesystem.mapper.RoleAuthorityMapper;
import com.digitaldeparturesystem.pojo.Authorities;

import java.util.List;

public class AuthorityTreeUtils {

    public static void getChildrenToMenu(RoleAuthorityMapper roleAuthorityMapper, Authorities authorities) {
        List<Authorities> children = roleAuthorityMapper.findChildrenByParentId(authorities.getId());
        authorities.setChildren(children);
        for (Authorities child : children) {
            getChildrenToMenu(roleAuthorityMapper,child);
        }
    }

}
