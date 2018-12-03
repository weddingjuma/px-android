package com.mercadopago.android.px.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mercadopago.android.px.internal.util.TextUtil;
import java.util.Date;

public class Token implements CardInformation {

    private String id;
    private String publicKey;
    private String cardId;
    private String luhnValidation;
    private String status;
    private String usedDate;
    private Integer cardNumberLength;
    private Date creationDate;
    private String truncCardNumber;
    private Integer securityCodeLength;
    private Integer expirationMonth;
    private Integer expirationYear;
    private Date lastModifiedDate;
    private Date dueDate;
    private String firstSixDigits;
    private String lastFourDigits;
    private Cardholder cardholder;
    private String esc;

    @Override
    public Integer getSecurityCodeLength() {
        return securityCodeLength;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setSecurityCodeLength(Integer securityCodeLength) {
        this.securityCodeLength = securityCodeLength;
    }

    @Override
    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    @Override
    public Integer getExpirationYear() {
        return expirationYear;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCardId() {
        return cardId;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getLuhnValidation() {
        return luhnValidation;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setLuhnValidation(String luhnValidation) {
        this.luhnValidation = luhnValidation;
    }

    public String getStatus() {
        return status;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsedDate() {
        return usedDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    public Integer getCardNumberLength() {
        return cardNumberLength;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCardNumberLength(Integer cardNumberLength) {
        this.cardNumberLength = cardNumberLength;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTruncCardNumber() {
        return truncCardNumber;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setTruncCardNumber(String truncCardNumber) {
        this.truncCardNumber = truncCardNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String getFirstSixDigits() {
        return firstSixDigits;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setFirstSixDigits(String firstSixDigits) {
        this.firstSixDigits = firstSixDigits;
    }

    @Override
    public String getLastFourDigits() {
        return lastFourDigits;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    @Override
    public Cardholder getCardHolder() {
        return cardholder;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCardholder(Cardholder cardholder) {
        this.cardholder = cardholder;
    }

    public String getEsc() {
        return esc;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setEsc(String esc) {
        this.esc = esc;
    }

    /**
     * @return json string
     * @deprecated Should not be it's responsibility - will delete it.
     */
    @Deprecated
    public String toJson() {
        Gson gson =
            new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        return gson.toJson(this);
    }

    public boolean isTokenValid() {
        return getLastFourDigits() != null && !TextUtil.isEmpty(getLastFourDigits());
    }
}