package com.cretin.www.caipu.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 2017/3/20.
 */

public class LikeModel extends BmobObject{

    /**
     * id : 472
     * caipu_id : 4362
     * title : 咸香土豆芝士曲奇
     * tags : 传统西餐;烘焙;甜品;增肥;甜;咸香;半小时-1小时;煮;烤;简单;饼干;烤箱;冬季;芝士;香甜;1-2人;健脾;祛风散寒;祛寒;健脾胃;1小时-2小时;助睡眠;脾虚
     * imtro : 很久没有碰烤箱了。这次为了尽快解决家里的芝士，又开始动手。 这次的曲奇有浓郁的奶香芝士味，还夹杂着葱香味，薯香味。哈哈，象我喜欢咸味的朋友有口福了。
     * ingredients : 土豆,200g;奶油芝士,150g;低筋面粉,100g
     * burden : 盐,适量;葱,15g
     * albums : http://img.juhe.cn/cookbook/t/5/4362_653505.jpg
     * passed : 0
     * user_upload : 0
     * steps : [{"step":"1.原料集合；","img":"http://img.juhe.cn/cookbook/s/44/4362_c2c180743158786d.jpg"},{"step":"2.土豆剥皮后煮熟或用微波炉转熟备用，葱切成葱花；","img":"http://img.juhe.cn/cookbook/s/44/4362_f22951a68568da21.jpg"},{"step":"3.将土豆泥，芝士，放入大保鲜袋内，用擀面杖擀成泥状，加入适量盐和面粉及葱花用手揉匀；","img":"http://img.juhe.cn/cookbook/s/44/4362_1938af93ced49b99.jpg"},{"step":"4.将做好的芝士土豆泥装入裱花袋，装好花型裱花嘴，在垫有油纸的烤盘上，将芝士土豆泥裱成玫瑰花型；","img":"http://img.juhe.cn/cookbook/s/44/4362_b2651a66a88a751e.jpg"},{"step":"5.烤箱预热180度，将烤盘放入第一层18-20分钟，表层上色即可；","img":"http://img.juhe.cn/cookbook/s/44/4362_4fa914bcb28957ba.jpg"},{"step":"6.出炉成品。","img":"http://img.juhe.cn/cookbook/s/44/4362_0ee6f6279fcde53d.jpg"}]
     * works : []
     */

    private String id;
    private String caipu_id;
    private String title;
    private String tags;
    private String imtro;
    private String ingredients;
    private String burden;
    private String albums;
    private String passed;
    private String user_upload;
    private List<StepsBean> steps;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaipu_id() {
        return caipu_id;
    }

    public void setCaipu_id(String caipu_id) {
        this.caipu_id = caipu_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImtro() {
        return imtro;
    }

    public void setImtro(String imtro) {
        this.imtro = imtro;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getBurden() {
        return burden;
    }

    public void setBurden(String burden) {
        this.burden = burden;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }

    public String getPassed() {
        return passed;
    }

    public void setPassed(String passed) {
        this.passed = passed;
    }

    public String getUser_upload() {
        return user_upload;
    }

    public void setUser_upload(String user_upload) {
        this.user_upload = user_upload;
    }

    public List<StepsBean> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsBean> steps) {
        this.steps = steps;
    }

    public static class StepsBean {
        /**
         * step : 1.原料集合；
         * img : http://img.juhe.cn/cookbook/s/44/4362_c2c180743158786d.jpg
         */

        private String step;
        private String img;

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public StepsBean() {
        }

        public StepsBean(String img, String step) {
            this.img = img;
            this.step = step;
        }
    }
}
