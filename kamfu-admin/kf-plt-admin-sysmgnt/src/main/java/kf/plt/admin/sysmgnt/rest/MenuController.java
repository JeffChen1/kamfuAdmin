/*
 *  Copyright (C) 2018  Wanghaobin<463540703@qq.com>

 *  AG-Enterprise 企业版源码
 *  郑重声明:
 *  如果你从其他途径获取到，请告知老A传播人，奖励1000。
 *  老A将追究授予人和传播人的法律责任!

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package kf.plt.admin.sysmgnt.rest;

import kf.plt.admin.common.msg.ListResultResponse;
import kf.plt.admin.sysmgnt.biz.MenuBiz;
import kf.plt.admin.sysmgnt.biz.UserBiz;
import kf.plt.admin.sysmgnt.constant.AdminCommonConstant;
import kf.plt.admin.sysmgnt.entity.Menu;
import kf.plt.admin.sysmgnt.rpc.service.PermissionService;
import kf.plt.admin.sysmgnt.vo.MenuTree;
import kf.plt.admin.auth.client.annotation.CheckClientToken;
import kf.plt.admin.auth.client.annotation.CheckUserToken;
import kf.plt.admin.common.rest.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @version 2017-06-12 8:49
 */
@Controller
@RequestMapping("menu")
@CheckUserToken
@CheckClientToken
public class MenuController extends BaseController<MenuBiz, Menu> {
    @Autowired
    private UserBiz userBiz;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ListResultResponse<Menu> list(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return new ListResultResponse<Menu>().data(baseBiz.selectByExample(example));
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public ListResultResponse<MenuTree> getTree(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return new ListResultResponse<MenuTree>().data(MenuTree.buildTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT));
    }

    /**
     *@Description: 根据验证的用户信息获取菜单列表
     *
     *@Author: wangs
     *
     *@Date 2018/8/28
     */
    @RequestMapping(value = "/menuAll", method = RequestMethod.GET)
    @ResponseBody
    public ListResultResponse<Menu> getMenuAll() throws Exception{
        return new ListResultResponse<Menu>().data(permissionService.getMenuByUserId());
    }

//    @RequestMapping(value = "/system", method = RequestMethod.GET)
//    @ResponseBody
//    public List<Menu> getSystem() {
//        Menu menu = new Menu();
//        menu.setParentId(AdminCommonConstant.ROOT);
//        return baseBiz.selectList(menu);
//    }

//    @RequestMapping(value = "/menuTree", method = RequestMethod.GET)
//    @ResponseBody
//    public List<MenuTree> listMenu(String parentId) {
//        try {
//            if (parentId == null) {
//                parentId = this.getSystem().get(0).getId();
//            }
//        } catch (Exception e) {
//            return new ArrayList<MenuTree>();
//        }
//        List<MenuTree> trees = new ArrayList<MenuTree>();
//        MenuTree node = null;
//        Example example = new Example(Menu.class);
//        Menu parent = baseBiz.selectById(parentId);
//        example.createCriteria().andLike("path", parent.getPath() + "%").andNotEqualTo("id",parent.getId());
//        return getMenuTree(baseBiz.selectByExample(example), parent.getId());
//    }

//    @RequestMapping(value = "/authorityTree", method = RequestMethod.GET)
//    @ResponseBody
//    public List<AuthorityMenuTree> listAuthorityMenu() {
//        List<AuthorityMenuTree> trees = new ArrayList<AuthorityMenuTree>();
//        AuthorityMenuTree node = null;
//        for (Menu menu : baseBiz.selectListAll()) {
//            node = new AuthorityMenuTree();
//            node.setText(menu.getTitle());
//            BeanUtils.copyProperties(menu, node);
//            trees.add(node);
//        }
//        return TreeUtil.bulid(trees, AdminCommonConstant.ROOT, null);
//    }
//
//    @RequestMapping(value = "/user/authorityTree", method = RequestMethod.GET)
//    @ResponseBody
//    public List<MenuTree> listUserAuthorityMenu(String parentId){
//        String userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
//        try {
//            if (parentId == null) {
//                parentId = this.getSystem().get(0).getId();
//            }
//        } catch (Exception e) {
//            return new ArrayList<MenuTree>();
//        }
//        return getMenuTree(baseBiz.getUserAuthorityMenuByUserId(userId),parentId);
//    }
//
//    @RequestMapping(value = "/user/system", method = RequestMethod.GET)
//    @ResponseBody
//    public List<Menu> listUserAuthoritySystem() {
//        String userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
//        return baseBiz.getUserAuthoritySystemByUserId(userId);
//    }

//    private List<MenuTree> getMenuTree(List<Menu> menus,String root) {
//        List<MenuTree> trees = new ArrayList<MenuTree>();
//        MenuTree node = null;
//        for (Menu menu : menus) {
//            node = new MenuTree();
//            BeanUtils.copyProperties(menu, node);
//            node.setLabel(menu.getTitle());
//            trees.add(node);
//        }
//        return TreeUtil.bulid(trees,root,  new Comparator<MenuTree>() {
//            @Override
//            public int compare(MenuTree o1, MenuTree o2) {
//                return o1.getOrderNum() - o2.getOrderNum();
//            }
//        }) ;
//    }


}
