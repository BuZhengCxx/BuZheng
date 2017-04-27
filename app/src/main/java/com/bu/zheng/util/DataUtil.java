package com.bu.zheng.util;

/**
 * Created by BuZheng on 2017/4/27.
 */

public class DataUtil {

    private static final String[] IMGS_16x9 = new String[]{
            "https://easyread.nosdn.127.net/pic20170426bfabafcfb1c24c969be27ad7d3239377.jpg",
            "https://easyread.nosdn.127.net/pic20170421cefd7bb3947044ee9a81a2683374299b.jpg",
            "https://easyread.nosdn.127.net/pic201704243dfcb09b620049c698199f95e35ee241.jpg",
            "https://easyread.nosdn.127.net/pic20170418283540bd4bce40ccaaf848e67ce811d6.jpg",
            "https://easyread.nosdn.127.net/pic2017042226186c9c42c74140947b4956fb7a1602.jpg",
            "https://easyread.nosdn.127.net/pic201704217b9c0206405d40b19718fb4dbf2a91e9.jpg",
            "https://easyread.nosdn.127.net/pic2016121580430c7bada3477b9d8b3dd812b7a176.jpg",
            "https://easyread.nosdn.127.net/pic20161215982deef6adb44115926adf8d06b92880.jpg",
            "https://easyread.nosdn.127.net/pic2016121557d8cec6c6c84845ac38da386518b280.jpg",
            "https://easyread.nosdn.127.net/pic2016121594bdf17c9ebf4220abe72506f76b98bb.jpg",
            "https://easyread.nosdn.127.net/pic20161215e5037871fda44280b7ee47cb0ee9c7be.jpg",
            "https://easyread.nosdn.127.net/pic20161215f210665f520041b2a1d975371c063151.jpg",
            "https://easyread.nosdn.127.net/pic20161215cbd16c63913449949ee10a17839a7b23.jpg",
            "https://easyread.nosdn.127.net/pic20161215b6fe9985076d4319829e342685701c15.jpg",
            "https://easyread.nosdn.127.net/pic20161215f032692d22664b78b2128ce409a673e9.jpg",
            "https://easyread.nosdn.127.net/pic201612154f9c0e1e73a74e98859492382f4de3f0.jpg",
            "https://easyread.nosdn.127.net/pic201612154036a1a26b5b4359a844c8a9505f5859.jpg",
            "https://easyread.nosdn.127.net/pic201612158f598619f70146bcb72b8fa33731ca3c.jpg",
            "https://easyread.nosdn.127.net/pic20161215c6cb31e643d8499e8469c349fa887bb6.jpg",
            "https://easyread.nosdn.127.net/pic2016121570a5d4ad8c4a4bf3ad3acead2885a926.jpg",
            "https://easyread.nosdn.127.net/pic201612156f87514cf5c14ce2bc520435880061cc.jpg",
            "https://easyread.nosdn.127.net/pic20161215a7fd6a95eb7a4bbc9188c385b82b07f9.jpg",
            "https://easyread.nosdn.127.net/pic20161215c62efac0f5eb4f2995226d991c657052.jpg",
            "https://easyread.nosdn.127.net/pic201612155984eb1f860c4ae8933de26f3e3ccc20.jpg"
    };

    private static final String[] IMGS_21x9 = new String[]{
            "https://easyread.nosdn.127.net/pic20170408d4db8cc030734b49bc5d0b96d4dc33c2.jpg",
            "https://easyread.nosdn.127.net/pic2017041016959595cc9947488cb8a7c66bbae295.jpg",
            "https://easyread.nosdn.127.net/pic2017042577bc192d07a645738f450287b9e631e5.jpg",
            "https://easyread.nosdn.127.net/pic201704165a8097a014734aedbc610e07725d1640.jpg",
            "https://easyread.nosdn.127.net/pic2017022474cd52bd2e844d74aa20b3f3d9ebae1d.jpg",
            "https://easyread.nosdn.127.net/pic/2016/11/29/bb8f309cc6e047838b57c2bba178543d.jpg",
            "https://easyread.nosdn.127.net/pic/2016/12/01/5bca440250464fc58b4fdd39e96ef319.jpg",
            "https://easyread.nosdn.127.net/pic/2016/09/22/e3e021bca8794c2eb2011ac25b5d1928.jpg",
            "https://easyread.nosdn.127.net/pic/2016/08/05/0e61f5274b5040af8cb6965b16c923a6.jpg",
            "https://easyread.nosdn.127.net/pic201704270cc7309e0e524777b2626aa69ee40727.jpg",
            "https://easyread.nosdn.127.net/pic20170420d903c3edddcd44ab9d76a61eb91fc5bf.gif",
            "https://easyread.nosdn.127.net/pic20170122c11c8ecebba14646b49c7feca0f4822e.jpg",
            "https://easyread.nosdn.127.net/pic/2016/07/11/b5e8b1d314c4471bac01e2950c68b650.jpg",
            "https://easyread.nosdn.127.net/pic/2016/11/17/ecb75ccc08b0437ebe3ab38f1c00a416.jpg",
            "https://easyread.nosdn.127.net/pic201703153110acba2768462198de9470d0950d76.jpg",
            "https://easyread.nosdn.127.net/pic/2016/11/10/6dedfba3f06f453bbae2ded3dfde05a5.jpg",
            "https://easyread.nosdn.127.net/pic/2016/12/09/34af924a6ccf4828aabe8d2367821374.jpg",
            "https://easyread.nosdn.127.net/pic20161212a4fa6d0da2b446af80b0fcda60117845.jpg",
            "https://easyread.nosdn.127.net/pic/2016/11/17/5dbfba371cba431a94b4483ff4c5fe3b.jpg",
            "https://easyread.nosdn.127.net/pic/2016/07/12/fe1cdf5c4369496e94205ee667fd6d44.jpg",
            "https://easyread.nosdn.127.net/pic2017021389631009ea4c459482e1ab5dffc966c4.jpg",
            "https://easyread.nosdn.127.net/pic/2016/11/17/b0c210ca8af148078348874c440b1cf0.jpg",
            "https://easyread.nosdn.127.net/pic/2016/11/07/4ab3414eeb1f4e1a8cf3bb55a03ff4cd.jpg",
            "https://easyread.nosdn.127.net/pic2017021381457f073852452fb3855904aa3d1d1b.jpg"
    };

    private static final String[] IMGS_COVER = new String[]{
            "https://easyread.nosdn.127.net/pic/2016/03/08/103cda4afde3411db1f0cfcad907ac21.jpg",
            "https://easyread.nosdn.127.net/pic201609081bcac10dae324f0287417daea25cff7a.jpg",
            "https://easyread.nosdn.127.net/pic/2015/10/27/4ba4b6b67c9c43e6b0af3b2fc304a401.jpg",
            "https://easyread.nosdn.127.net/pic20160602d189dd14166e496796c84a51ff22aca3.jpeg",
            "https://easyread.nosdn.127.net/pic/2015/12/04/355f79d4666141beb7be7529fb0d12d0.jpg",
            "https://easyread.nosdn.127.net/pic20160229a8de1d6c9c8d4177a076f5b7827a23b4.jpeg",
            "https://easyread.nosdn.127.net/pic201601251271f0eae71242db9e5b2a3d1a942670.jpeg",
            "https://easyread.nosdn.127.net/pic20160125330eaf29ed0d437282f0cfe533237cd5.jpeg",
            "https://easyread.nosdn.127.net/pic/2015/10/29/8a766df3d4cd4173a3e7eb383fa9a01a.jpg",
            "https://easyread.nosdn.127.net/pic201604283e1b9ec1816d4036bc2988ccb12f2355.jpeg",
            "https://easyread.nosdn.127.net/pic20160927182dcaa1fc8d44119898de20a6674edb.jpg",
            "https://easyread.nosdn.127.net/pic201601257f431eb73719448bbab53aca5668278e.jpeg",
            "https://easyread.nosdn.127.net/pic20160712eaeef20188624ab3bb0c7d3ad8027013.jpg",
            "https://easyread.nosdn.127.net/pic20160125ead1f92d76c84d73a09ae62106e9c16d.jpeg",
            "https://easyread.nosdn.127.net/pic2016090107985b533a3f406e943496937424a6b0.jpg",
            "https://easyread.nosdn.127.net/pic20160201e382642ad50e49cbba7a69331d8f251e.jpeg",
            "https://easyread.nosdn.127.net/pic/2015/12/04/9bfb214f026540c3a970b0a1fc633558.jpg",
            "https://easyread.nosdn.127.net/pic20160927e2c94e47bf8f4c54b4f544f495f7bad8.jpg",
            "https://easyread.nosdn.127.net/pic20160930e45661b64d39489d8b8c064c7f2b3adb.jpg",
            "https://easyread.nosdn.127.net/pic2016033162244ab3e82e49e8abe262b4f7463343.jpeg",
            "https://easyread.nosdn.127.net/pic201603314ab95f2bde65401db34de397949f4804.jpeg",
            "https://easyread.nosdn.127.net/pic20160125cffbbb1d126341ff9c9f306388ce2797.jpeg",
            "https://easyread.nosdn.127.net/pic201608223c3205f2ba1a4a2e9d40eb4ff00c22b2.jpg",
            "https://easyread.nosdn.127.net/pic20160301d893fff7c46d443f9462ed9a3d6e7b0c.jpeg"
    };

}
