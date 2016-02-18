package com.pxy.studyhelper.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pxy
 */
public class Tools4Sure {

    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean isValidEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        if (TextUtils.isEmpty(email))
            return false;
        else
            return m.matches();
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean existPackage(String packageName, Context context) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    /**
     * 判断是否是正确的手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isRightMobilePhoe(String phone) {
        if(phone == null){
            return false;
        }
        String head1 = "";
        String head2 = "";

        // 去除前后的空白
        phone = phone.trim();

        // 判断输入的电话号码是否合法
        if (phone.length() < 11) {
            return false;
        } else {
            // 处理国内的+86开头
            if (phone.startsWith("+")) {
                phone = phone.substring(1);
            }
            if (phone.startsWith("86")) {
                phone = phone.substring(2);
            }
        }
        // 去除+86后电话号码应为11位
        if (phone.length() != 11) {

            return false;
        }
        // 判断去除+86后剩余的是否全为数字
        if (!isNum(phone)) {
            System.out.println(" not num");
            return false;
        }
        // 截取前3或前4位电话号码，判断运营商
        head1 = phone.substring(0, 3);
        head2 = phone.substring(0, 4);

        // 移动前三位
        boolean cmcctemp3 = head1.equals("135") || head1.equals("136")
                || head1.equals("137") || head1.equals("138")
                || head1.equals("139") || head1.equals("147")
                || head1.equals("150") || head1.equals("151")
                || head1.equals("152") || head1.equals("157")
                || head1.equals("158") || head1.equals("159")
                || head1.equals("182") || head1.equals("187")
                || head1.equals("188");

        if (cmcctemp3) {
            return true;
        }
        // 移动前4位
        boolean cmcctemp4 = head2.equals("1340") || head2.equals("1341")
                || head2.equals("1342") || head2.equals("1343")
                || head2.equals("1344") || head2.equals("1345")
                || head2.equals("1346") || head2.equals("1347")
                || head2.equals("1348");

        if (cmcctemp4) {
            return true;
        }
        // 联通前3位
        boolean unicomtemp = head1.equals("130") || head1.equals("131")
                || head1.equals("132") || head1.equals("145")
                || head1.equals("155") || head1.equals("156")
                || head1.equals("185") || head1.equals("186");

        if (unicomtemp) {
            return true;
        }
        // 电信前3位
        boolean telecomtemp = head1.equals("133") || head1.equals("153")
                || head1.equals("180") || head1.equals("189") || head1.equals("181");

        if (telecomtemp) {
            return true;
        }
        //虚拟运营商
        boolean virtualtemp = head1.equals("170");

        if (virtualtemp) {
            return true;
        }

        return false;
    }

    /*
     * 判断输入的是否为数字
	 *
	 * @返回true说明是数字，false说明不全是数字
	 */
    private static boolean isNum(String phoneNum) {
        for (int i = 0; i < phoneNum.length(); i++) {
            if (!Character.isDigit(phoneNum.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
