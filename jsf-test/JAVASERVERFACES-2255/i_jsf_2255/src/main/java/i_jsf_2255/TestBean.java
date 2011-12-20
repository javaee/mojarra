package i_jsf_2255;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@RequestScoped
public class TestBean {

        private Boolean test = false;

        public Boolean getTest() {
            return test;
        }

        public void setTest(Boolean test) {
            this.test = test;
        }
}

