package org.spongycastle.jcajce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.CipherKeyGenerator;
import org.spongycastle.crypto.engines.NoekeonEngine;
import org.spongycastle.crypto.generators.Poly1305KeyGenerator;
import org.spongycastle.crypto.macs.GMac;
import org.spongycastle.crypto.macs.Poly1305;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.jcajce.provider.config.ConfigurableProvider;
import org.spongycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import org.spongycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.spongycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.spongycastle.jcajce.provider.symmetric.util.BaseMac;
import org.spongycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.spongycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;




public final class Noekeon
{
  private Noekeon() {}
  
  public static class ECB
    extends BaseBlockCipher
  {
    public ECB()
    {
      super()
      {
        public BlockCipher get()
        {
          return new NoekeonEngine();
        }
      };
    }
  }
  
  public static class KeyGen
    extends BaseKeyGenerator
  {
    public KeyGen()
    {
      super(128, new CipherKeyGenerator());
    }
  }
  
  public static class GMAC
    extends BaseMac
  {
    public GMAC()
    {
      super();
    }
  }
  
  public static class Poly1305
    extends BaseMac
  {
    public Poly1305()
    {
      super();
    }
  }
  
  public static class Poly1305KeyGen
    extends BaseKeyGenerator
  {
    public Poly1305KeyGen()
    {
      super(256, new Poly1305KeyGenerator());
    }
  }
  
  public static class AlgParamGen
    extends BaseAlgorithmParameterGenerator
  {
    public AlgParamGen() {}
    
    protected void engineInit(AlgorithmParameterSpec genParamSpec, SecureRandom random)
      throws InvalidAlgorithmParameterException
    {
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Noekeon parameter generation.");
    }
    
    protected AlgorithmParameters engineGenerateParameters()
    {
      byte[] iv = new byte[16];
      
      if (random == null)
      {
        random = new SecureRandom();
      }
      
      random.nextBytes(iv);
      


      try
      {
        AlgorithmParameters params = createParametersInstance("Noekeon");
        params.init(new IvParameterSpec(iv));
      }
      catch (Exception e)
      {
        throw new RuntimeException(e.getMessage());
      }
      AlgorithmParameters params;
      return params;
    }
  }
  
  public static class AlgParams extends IvAlgorithmParameters
  {
    public AlgParams() {}
    
    protected String engineToString() {
      return "Noekeon IV";
    }
  }
  
  public static class Mappings
    extends SymmetricAlgorithmProvider
  {
    private static final String PREFIX = Noekeon.class.getName();
    


    public Mappings() {}
    

    public void configure(ConfigurableProvider provider)
    {
      provider.addAlgorithm("AlgorithmParameters.NOEKEON", PREFIX + "$AlgParams");
      
      provider.addAlgorithm("AlgorithmParameterGenerator.NOEKEON", PREFIX + "$AlgParamGen");
      
      provider.addAlgorithm("Cipher.NOEKEON", PREFIX + "$ECB");
      
      provider.addAlgorithm("KeyGenerator.NOEKEON", PREFIX + "$KeyGen");
      
      addGMacAlgorithm(provider, "NOEKEON", PREFIX + "$GMAC", PREFIX + "$KeyGen");
      addPoly1305Algorithm(provider, "NOEKEON", PREFIX + "$Poly1305", PREFIX + "$Poly1305KeyGen");
    }
  }
}
