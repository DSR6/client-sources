package org.spongycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Hashtable;
import org.spongycastle.asn1.ASN1Enumerated;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.util.Integers;






















public class CRLReason
  extends ASN1Object
{
  /**
   * @deprecated
   */
  public static final int UNSPECIFIED = 0;
  /**
   * @deprecated
   */
  public static final int KEY_COMPROMISE = 1;
  /**
   * @deprecated
   */
  public static final int CA_COMPROMISE = 2;
  /**
   * @deprecated
   */
  public static final int AFFILIATION_CHANGED = 3;
  /**
   * @deprecated
   */
  public static final int SUPERSEDED = 4;
  /**
   * @deprecated
   */
  public static final int CESSATION_OF_OPERATION = 5;
  /**
   * @deprecated
   */
  public static final int CERTIFICATE_HOLD = 6;
  /**
   * @deprecated
   */
  public static final int REMOVE_FROM_CRL = 8;
  /**
   * @deprecated
   */
  public static final int PRIVILEGE_WITHDRAWN = 9;
  /**
   * @deprecated
   */
  public static final int AA_COMPROMISE = 10;
  public static final int unspecified = 0;
  public static final int keyCompromise = 1;
  public static final int cACompromise = 2;
  public static final int affiliationChanged = 3;
  public static final int superseded = 4;
  public static final int cessationOfOperation = 5;
  public static final int certificateHold = 6;
  public static final int removeFromCRL = 8;
  public static final int privilegeWithdrawn = 9;
  public static final int aACompromise = 10;
  private static final String[] reasonString = { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
  





  private static final Hashtable table = new Hashtable();
  
  private ASN1Enumerated value;
  
  public static CRLReason getInstance(Object o)
  {
    if ((o instanceof CRLReason))
    {
      return (CRLReason)o;
    }
    if (o != null)
    {
      return lookup(ASN1Enumerated.getInstance(o).getValue().intValue());
    }
    
    return null;
  }
  

  private CRLReason(int reason)
  {
    value = new ASN1Enumerated(reason);
  }
  

  public String toString()
  {
    int reason = getValue().intValue();
    String str; String str; if ((reason < 0) || (reason > 10))
    {
      str = "invalid";
    }
    else
    {
      str = reasonString[reason];
    }
    return "CRLReason: " + str;
  }
  
  public BigInteger getValue()
  {
    return value.getValue();
  }
  
  public ASN1Primitive toASN1Primitive()
  {
    return value;
  }
  
  public static CRLReason lookup(int value)
  {
    Integer idx = Integers.valueOf(value);
    
    if (!table.containsKey(idx))
    {
      table.put(idx, new CRLReason(value));
    }
    
    return (CRLReason)table.get(idx);
  }
}
