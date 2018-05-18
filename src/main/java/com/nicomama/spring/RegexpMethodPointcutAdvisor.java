package com.nicomama.spring;

public class RegexpMethodPointcutAdvisor extends org.springframework.aop.support.RegexpMethodPointcutAdvisor {
    private String packageScan;

    public RegexpMethodPointcutAdvisor(String packageScan) {
        super(new Interceptor());
        super.setPattern(packageScan);
        this.packageScan = packageScan;
    }

    public RegexpMethodPointcutAdvisor() {
        super(new Interceptor());
    }

    public void setPackageScan(String packageScan) {
        super.setPattern(packageScan);
        this.packageScan = packageScan;
    }
}
