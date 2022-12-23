package passi.skittleclicker.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Shop{
    private long skittles = 0;
    private static final long baseSkittlesPerClick = 1;
    private double clickModifier = 1;
    private double goldenModifier = 1.5;
    private double milkModifier = 1;
    private double milkClicksMod = 0;
    private final double MAX_MILK_CLICKS_MOD = 1;
    private final double MILK_CLICKS_MOD_INCREMENT = 0.01;
    private double generalModifier = 1;
    private boolean goldenActive;
    private final float updateVisibleFraction = 0.5f; //TODO subject to change probably between .3 and 1
    List<ShopGroup> shopGroups;
    List<Upgrade> upgrades;
    List<Integer> alreadyDisplayedUpgrades = new ArrayList<>();
    private final long[] shopGroupSkittles;
    private final long nextShopGroupMultiplier = 4;
    private final long[] costMultipliers;
    private final double upgradeCostMultiplier = 2;
    private final boolean testing = true; //TODO dont forget to set to false after sending to purr
    private final List<String> barVisitorNames;

    public Shop(){
        shopGroupSkittles = new long[11];
        costMultipliers = new long[shopGroupSkittles.length];
        for (int i = 0; i < shopGroupSkittles.length; i++) {
            shopGroupSkittles[i] = Math.round(Math.pow(nextShopGroupMultiplier, i));
            costMultipliers[i] = Math.round(120 * (1 + (i * 1.3))); //(Math.pow(1.3, i) ?
        }

        ShopGroup clickerShopGroup = new ShopGroup(ShopGroup.Type.CLICKER, shopGroupSkittles[0],
                36,shopGroupSkittles[0]*costMultipliers[0], """ 
                Why click yourself when you can just let others do it?
                People from chat come together and help you in
                making more and more skittles! 
                Spam those left mouse buttons for maximum skittles output.""");
        ShopGroup grannyShopGroup = new ShopGroup(ShopGroup.Type.GRANNY, shopGroupSkittles[1],
                24, shopGroupSkittles[1]*costMultipliers[1],
                testing ? "Local Neighbourhood grandmas help\nyou the with skittles production" :
                """
                A certain Cat with a Knight Title forces
                I mean politely asks the local neighbourhood grandmas
                to help with the skittles production""");
        ShopGroup duckShopGroup = new ShopGroup(ShopGroup.Type.DUCK, shopGroupSkittles[2],
                18, shopGroupSkittles[2]*costMultipliers[2], """
                In a remote skittles swamp the rumored skittles duck
                with a normal amount of legs can be found.
                It grants you an increase in skittles production
                by 4 Skittles per Second for every leg it possesses.
                +16 to SpS
                
                (it is said the more legs a duck has, the more
                skittles it produces)\s""");
        ShopGroup skittlesBarShopGroup = new ShopGroup(ShopGroup.Type.SKITTLES_BAR, shopGroupSkittles[3],
                96, shopGroupSkittles[3]*costMultipliers[3], """
                You start your own bar to share the sweet flavor of
                skittles drinks with many people!
                Somehow you still end up making more skittles with every
                bar you open despite them being your main ingredient.
                
                Some familiar names might show up in the bar.
                (You might wanna hover over the individual drinks)""");

        ShopGroup mountainShopGroup = new ShopGroup(ShopGroup.Type.MOUNTAIN, shopGroupSkittles[4],
                24, shopGroupSkittles[4]*costMultipliers[4], """
                It is said that Skittle Mountain holds many secrets. 
                It may be challenging to climb but the strawberreries
                growing on it are an excellent ingredient for skittles.
                And the best part? You can climb the mountain multiple times
                and keep getting more strawbs.""");
        ShopGroup collabShopGroup = new ShopGroup(ShopGroup.Type.COLLAB, shopGroupSkittles[5],
                24, shopGroupSkittles[5]*costMultipliers[5], """
                Live streaming skittles production seems to yield even more
                delicious skittles!
                Get together with your streaming friends for a big collab
                and turn those virtual skittles into real ones.""");
        ShopGroup cyberpunkBarShopGroup = new ShopGroup(ShopGroup.Type.CYBERPUNK_BAR, shopGroupSkittles[6], 15,
                shopGroupSkittles[6]*costMultipliers[6], """
                You already own skittles bars, but do you own
                cyberpunk skittles bars? 
                Hire knowledgeable people to work for you 
                and contract them to create highly profitable drinks.
                Just tell them to not overdo it with the Karmotrine.""");
        ShopGroup isekaiShopGroup = new ShopGroup(ShopGroup.Type.ISEKAI, shopGroupSkittles[7],
                24, shopGroupSkittles[7]*costMultipliers[7], """
                The skittle reserves of this world are no longer enough for you.
                Since you heard that in other worlds there might be magic,
                you need to explore this option. 
                If you're lucky you might even find someone 
                that wields the highest form of attack magic:
                Skittle Explosion Magic. EKSU PURO SION""");
        ShopGroup tailorGroup = new ShopGroup(ShopGroup.Type.TAILOR, shopGroupSkittles[8],
                24, shopGroupSkittles[8]*costMultipliers[8], """
                Skilled Tailors, Making skittles clothes out of 
                the Fabric of reality. Some can even harness the powers 
                of those skittle dresses and wear them, 
                granting them increased strength in combat.""");
        ShopGroup dragonShopGroup = new ShopGroup(ShopGroup.Type.DRAGON, shopGroupSkittles[9],
                24, shopGroupSkittles[9]*costMultipliers[9], """
                If you were thinking this game starts to drag on... I got you!
                Dragon is the next way you can increase your skittles production.
                More specifically skittles synthesized from the rare ingredient
                dragon tail. Most dragons would never allow you touch to their tail,
                but some are almost as enthusiastic about you eating their tail as
                they are about wearing a maid outfit.""");
        ShopGroup danceFloorShopGroup = new ShopGroup(ShopGroup.Type.DANCE_FLOOR, shopGroupSkittles[10],
                24, shopGroupSkittles[10]*costMultipliers[10], """
                HASHIRE SORI YO
                KAZE NO YOU NI
                TSUKIMIHARA WO
                PADORU PADORU""");

        //Order in which shopGroups are added has to be the same as order of buttons #jank
        shopGroups = new ArrayList<>();
        shopGroups.add(clickerShopGroup);
        shopGroups.add(grannyShopGroup);
        shopGroups.add(duckShopGroup);
        shopGroups.add(skittlesBarShopGroup);
        shopGroups.add(mountainShopGroup);
        shopGroups.add(collabShopGroup);
        shopGroups.add(cyberpunkBarShopGroup);
        shopGroups.add(isekaiShopGroup);
        shopGroups.add(tailorGroup);
        shopGroups.add(dragonShopGroup);
        shopGroups.add(danceFloorShopGroup);

        //Upgrades
        upgrades = new ArrayList<>();
        int[] milkStateIndices = new int[MilkState.State.values().length];
        upgrades.add(new Upgrade(ShopGroup.Type.MILK,"Milk", 750, 2,
                shopgroupTypeToString(ShopGroup.Type.MILK) + "Upgrade #1\n\n" +
                        """
                        Whats better than a skittle?
                        Right, a skittle dunked into milk.
                        
                        With this upgrade you unlock milk, which
                        causes your consecutive clicks to be worth 
                        more skittles. 
                        The more you click, the more milk you get.
                        And more milk means more skittles per click.""", "upgrades/milk_normal.png"));
        milkStateIndices[0] = upgrades.size() - 1;
        upgrades.add(new Upgrade(ShopGroup.Type.MILK,"Choccy Milk", 5000, 2,
                shopgroupTypeToString(ShopGroup.Type.MILK) + "Upgrade #2\n\n" +
                        """
                        The combination of skittles and milk can only 
                        be topped if you add chocolate to the mix.
                        Choccy milk is superior to normal milk in 
                        every regard after all.
                        Upgrade your milk to choccy milk and
                        
                        +100% max consecutive click multiplier""", "upgrades/milk_choccy.png"));
        milkStateIndices[1] = upgrades.size() - 1;
        upgrades.add(new Upgrade(ShopGroup.Type.MILK,"Macha Milk", 50000, 2,
                shopgroupTypeToString(ShopGroup.Type.MILK) + "Upgrade #3\n\n" +
                        """
                        Being a certified weeb, you know the exquisite
                        tea, that is macha. Also goes great with milk.
                        So here you go. Macha milk for all the land of the
                        rising sun enthusiasts.
                        Upgrade your milk to macha milk and
                        
                        +100% max consecutive click multiplier""", "upgrades/milk_macha.png"));
        milkStateIndices[2] = upgrades.size() - 1;
        upgrades.add(new Upgrade(ShopGroup.Type.MILK,"Superior Milk", 500000, 4,
                shopgroupTypeToString(ShopGroup.Type.MILK) + "Upgrade #4\n\n" +
                        """
                        How could you ever top macha milk?
                        I dont think there is any kind of milk 
                        that has been forgotten so this upgrade
                        is purely cosmetic and does not change your milk
                        at all in any way.
                        
                        
                        ... Ok i lied, enjoy your catgirl milk you nerds.
                        
                        +300% max consecutive click multiplier""", "upgrades/milk_catgirl.png"));
        milkStateIndices[3] = upgrades.size() - 1;
        MilkState.setUpStates(milkStateIndices);

        upgrades.add(new Upgrade(ShopGroup.Type.ALL,"Better production", 1000, 1.1,
                shopgroupTypeToString(ShopGroup.Type.ALL) + " Upgrade #1\n\n" +
                        """
                        The rounder the skittles the better!
                        And the only thing rounder than a skittle is a plu. 
                        Increase your skittle production 
                        by trying to emulate the superior shape
                        
                        +10% to all skittles gains""", "upgrades/all_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ALL,"Better production", 10000, 1.1,
                shopgroupTypeToString(ShopGroup.Type.ALL) + " Upgrade #2\n\n" +
                        """
                        Your skittles still need to become rounder!
                        Hire top notch scientist to research the
                        origin of all that is round. Or as they call it,
                        the plurigin.
                        
                        +10% to all skittles gains""", "upgrades/all_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ALL,"Better production", 100000, 1.3,
                shopgroupTypeToString(ShopGroup.Type.ALL) + " Upgrade #3\n\n" +
                        """
                        This is it! Your skittles can't possibly become
                        any rounder! With this improvement you might just hit
                        the non-plu-ultra. 
                        The pinnacle of roundness is achieved.
                        
                        +30% to all skittles gains""", "upgrades/all_gold.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Left Ear of the Forbidden One", 10000000000L, 5,
                shopgroupTypeToString(ShopGroup.Type.ALL) + " Upgrade \n\n" +
                        """
                        You were thinking a plu is the roundest thing
                        there will ever be.
                        But you discover an entity so perfectly round,
                        that reality bends itself around it.
                        Forming a giant black hole size circle.
                        If you get this and the Right Ear of the 
                        Forbidden One, you will gain unlimited power!
                        
                        +400% to all skittles gains""", "upgrades/left_ear.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Right Ear of the Forbidden One", 10000000000L, 5,
                shopgroupTypeToString(ShopGroup.Type.ALL) + " Upgrade \n\n" +
                        """
                        You were thinking a plu is the roundest thing
                        there will ever be.
                        But you discover an entity so perfectly round,
                        that reality bends itself around it.
                        Forming a giant black hole size circle.
                        If you get this and the Left Ear of the 
                        Forbidden One, you will gain unlimited power!
                        
                        +400% to all skittles gains""", "upgrades/right_ear.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Bronze", 500, 2,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #1\n\n" +
                        """
                        Clicking is a great way to gain skittles.
                        But a single mouse quickly reaches the limits of how
                        many can be gained.
                        With this upgrade an evil mousie clone materializes,
                        helping you gain more skittles per click. 
                        
                        +100% more skittles gained from clicking""", "upgrades/player_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Silver", 7500, 2,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #2\n\n" +
                        """
                        Evil Mousie has reached a frightening amount of
                        self initiative. She even has her own 
                        discord account since the 2 pc setup.
                        She clones herself and makes even 
                        more evil Mousies.
                        
                        +100% more skittles gained from clicking """, "upgrades/player_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Gold", 75000, 2,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #3\n\n" +
                        """
                        The evil mousies have formed a council.
                        Thats probably a bad thing, but they don't
                        really include you in their discussions so you
                        don't know what they are scheming.
                        Whatever they did, it seems to further boost your
                        skittles clicking power.
                        
                        +100% more skittles gained from clicking """, "upgrades/player_gold.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Diamond", 750000, 2, //TODO
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #4\n\n" +
                        """
                        The evil mousies have figured out a way to stop aging 
                        and stay 22 forever. They have founded the
                        Mousie Intergalactic Council of Knowing Everlasting Youth. 
                        Or M.I.C.K.E.Y. for short
                                                                
                        +300% more skittles gained from clicking""", "upgrades/player_diamond.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Emerald", 3000000, 2, //TODO
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #5\n\n" +
                        """
                        Nothing is safe from the evil mousies anymore.
                        They have taken over the universe and keep
                        making more and more skittles.
                                                                
                        +300% more skittles gained from clicking""", "upgrades/player_emerald.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Ruby", 10000000, 4,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #6\n\n" +
                        """
                        You can't understand how but the Mousie Clone
                        Council is still expanding their skittles production.
                                                                
                        +300% more skittles gained from clicking""", "upgrades/player_ruby.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Sapphire", 30000000, 4,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #7\n\n" +
                        """
                        H opefully you know the Mousie Clone Council.
                        E veryone likes them!
                        L oving them is a given. They are not evil after all.
                        P roducing more and more skittles surely helps everyone.
                                                                
                        +300% more skittles gained from clicking""", "upgrades/player_sapphire.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Amethyst", 100000000, 9,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Caesar #8\n\n" +
                        """
                        Lw vwb kwvbqvcm lwev bpqa xibp.
                        Vwbpqvo jcb uilvmaa (ivl asqbbtma) eqtt jm tmnb.
                        Bpqa qa i eizvqvo.
                        Bpm ncbczm pwtla vwbpqvo jcb asqbbtma.
                        Ivl bpmzm ewv'b jm ivg zwwu nwz ivgbpqvo mtam.
                                                             
                        +800% more skittles gained from clicking""", "upgrades/player_amethyst.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Player Clicks Void", 1000000000, 42.42,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + " Upgrade #9\n\n" +
                        """
                        The Mousie Council has taken over every atom 
                        in the universe and converted it into skittles.
                        So you just convert the void into skittles.
                        That is right, you found the true nature of dark matter,
                        and it's sweet and round!
                        Increase your skittles per click to the max.
                        
                        +4242% more skittles gained from clicking""", "upgrades/player_void.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Finale", 50000000000L, 100,
                 "Last Upgrade \n\n" +
                        """
                        Everything is skittles.
                        Heck even nothing is skittles now.
                        There is nothing else left.
                        If you can read this message it is already to late.
                        It only shows itself at the end.
                        The attempt to send an encrypted message to warn you
                        must have failed.
                        This is it.
                        
                        Buy to end the game.
                        (only unlocks if all other things have been bought)""", "upgrades/finale.png"));

        upgrades.add(new Upgrade(ShopGroup.Type.GOLDEN,"Better Gold Skittles", 30000, 1.2,
                shopgroupTypeToString(ShopGroup.Type.GOLDEN) + " Upgrade #1\n\n" +
                        """
                        Improving the output of golden skittles 
                        is no easy task. Only a few know how to do it.
                        You summon an adorable tanuki to help you out.
                        They seem to know a lot about gold
                        and offer you some advice.
                        
                        Increase golden skittle multiplier by 30%""", "upgrades/gold_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GOLDEN,"Better Gold Skittles", 300000, 1.2,
                shopgroupTypeToString(ShopGroup.Type.GOLDEN) + " Upgrade #2\n\n" +
                        """
                        You utilize the gold of some golden 
                        strawberrerries to make your golden skittles
                        even more refined!
                        
                        +30% increase in Golden Skittles multiplier""", "upgrades/gold_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GOLDEN,"Better Gold Skittles", 3000000, 1.2,
                shopgroupTypeToString(ShopGroup.Type.GOLDEN) + " Upgrade #3\n\n" +
                        """
                        With the power of KinKaiCookie 
                        I mean KinKaiSkittle, you further refine 
                        the gold in your golden skittles. 
                        
                        +30% increase in Golden Skittles multiplier""" , "upgrades/gold_gold.png"));

        //bronze upgrades
        upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Bronze Clicker",
                getShopGroupUpgradeCost(0, 1), 2,
                    shopgroupTypeToString(ShopGroup.Type.CLICKER) + " Bronze Upgrade\n\n" +
                            """
                           Your clickers are led by the powerful AlphaSkittle.
                           He has a reputation to click faster than his shadow,
                           especially if people in chat need to get banned.
                           While your clickers don't reach the same click speed, 
                           it still has a positive effect on them.
                           
                           +100% increase in Clicker skittles production""",
                "upgrades/clicker_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Bronze Granny",
                getShopGroupUpgradeCost(1, 1), 2,
                testing ? "Increases skittle production of all grannies by 100%" :
                shopgroupTypeToString(ShopGroup.Type.GRANNY) + " Bronze Upgrade\n\n" +
                        """
                       Nothing motivates grannies to produce more 
                       skittles quite as well as a ferocious cat 
                       threatening to crash into them with a bike.
                       These methods may seem questionable 
                       but the results speak for themselves.
                       
                       Increases skittle production of all grannies by 100%""",
                "upgrades/granny_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Bronze Ducks",
                getShopGroupUpgradeCost(2, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.DUCK) + " Bronze Upgrade\n\n" +
                        """
                       Contrary to most other ways to get skittles
                       you dont increase your skittles gain by
                       obtaining more ducks. No, you gain more skittles
                       the more legs your duck has.
                       This upgrade makes the legs of your duck 
                       regrow faster, so you can harvest more of 
                       those sweet skittles squeezed from their legs.
                       
                       +100% increase in skittles producktion""",
                "upgrades/duck_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Bronze Skittles Bar",
                getShopGroupUpgradeCost(3, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.SKITTLES_BAR) + " Bronze Upgrade\n\n" +
                        """
                       Every bar needs good bouncers to keep order and Peace.
                       You hire an expert bouncer who seems to be especially
                       good at that. Always keeping an eye out for troublesome
                       customers and making sure they don't cause any problems.
                       This makes the atmosphere in your bar nicer 
                       and attracts more customers.
                       
                       +100% increase in skittles bar production""",
                "upgrades/skittles bar_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.CYBERPUNK_BAR,"Bronze Cyberpunk Bar",
                getShopGroupUpgradeCost(6, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.CYBERPUNK_BAR) + " Bronze Upgrade\n\n" +
                        """
                       You expand your assortment of drinks to suit the
                       tastes of the Skittlim, humanoid robots that are as
                       close to humans as you could get.
                       This seems to be a success and attracts many of them,
                       especially a little red haired robot with a joyful
                       attitude and an equally perverted sense of humour.
                       
                       +100% increase in cyberpunk bar production""",
                "upgrades/cyberpunk bar_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.MOUNTAIN,"Bronze Mountain",
                getShopGroupUpgradeCost(4, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.MOUNTAIN) + " Bronze Upgrade\n\n" +
                        """
                        Collecting strawberrerries is a great way to get skittles, 
                        but climbing the mountain faster and faster also 
                        seems to yield a lot of skittles. Through a lot of 
                        training you improve your climbing skills and are now able 
                        to climb the mountain in only 42m 41s 594ms
                        
                        +100% increase in Mountain skittles production""",
                "upgrades/mountain_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.COLLAB,"Bronze Collab",
                getShopGroupUpgradeCost(5, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.COLLAB) + " Bronze Upgrade\n\n" +
                        """
                        A good collab needs music. You ask your streamer friend 
                        and pixel messiah, blue guy, for help and get some of 
                        the best rave music there is. Meow meow meow meow…
                                                                
                        +100% increase in skittles from collabs""",
                "upgrades/collab_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ISEKAI,"Bronze Isekai",
                getShopGroupUpgradeCost(7, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.ISEKAI) + " Bronze Upgrade\n\n" +
                        """
                       After arriving in this Wonderful World you receive God's Blessing. 
                       Or you thought it was god, but it is just a useless water arch priest.
                       Well nothing you can do about that. It seems to help your skittles
                       production anyway. Maybe this blue girl is not so useless after all...
                       
                       +100% increase in skittles from isekais""",
                "upgrades/isekai_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.TAILOR,"Bronze Tailor",
                getShopGroupUpgradeCost(8, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.TAILOR) + " Bronze Upgrade\n\n" +
                        """
                       Perfecting the art of combat in skittle fibre 
                       combat dresses may seem hard. But you can actually
                       spam two moves the whole time and you're gonna be fine.
                       Learn the basics of COUNTER HITTO and GAADO BUREKO.
                       
                       +100% increase in skittles from tailors""",
                "upgrades/tailor_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DRAGON,"Bronze Dragon",
                getShopGroupUpgradeCost(9, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.DRAGON) + " Bronze Upgrade\n\n" +
                        """
                        You meet a dragon that plays video games all day
                        and is very much interested in treasures.
                        You convince him that skittles are the greatest treasure
                        in the world and that he should help you get more!
                        
                        +100% increase in Dragon skittles production""",
                "upgrades/dragon_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DANCE_FLOOR,"Bronze Dance Floor",
                getShopGroupUpgradeCost(10, 1), 2,
                shopgroupTypeToString(ShopGroup.Type.DANCE_FLOOR) + " Bronze Upgrade\n\n" +
                        """
                        *padoruing intensifies*
                        
                        +100% increase in Dance Floor skittles production""",
                "upgrades/dance floor_bronze.png"));

        //silver upgrades
        upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Silver Clicker",
                getShopGroupUpgradeCost(0, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.CLICKER) + " Silver Upgrade\n\n" +
                        """
                       One of the clickers has started a skittle 
                       smuggling business. It seems to be going very well.
                       To the point that the skittles acquired by smuggling are just
                       as high in number as those earned by all other clickers. 
                       But that's just_a_general estimate.
                       
                       +100% increase in Clicker skittles production""",
                "upgrades/clicker_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Silver Granny",
                getShopGroupUpgradeCost(1, 2), 2,
                testing ? "Increases skittle production of all grannies by 100%" :
                shopgroupTypeToString(ShopGroup.Type.GRANNY) + " Silver Upgrade\n\n" +
                        """
                       Some of the grannies have started to turn
                       religious, worshipping their lord and saviour, the cabbit.
                       That does not fly well with their manager though.
                       Free time and talk not related to skittles is now banned.
                       
                       Increasing granny skittle production by 100%""",
                "upgrades/granny_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Silver Ducks",
                getShopGroupUpgradeCost(2, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.DUCK) + " Silver Upgrade\n\n" +"""
                       The legs of your duck need to grow even faster.
                       You show the duck gartic phone images of other
                       ducks with normal numbers of legs.
                       This helps motivate the duck to regrow its legs 
                       even faster. That and the preheated oven.
                       
                       +100% increase in skittles producktion""",
                "upgrades/duck_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Silver Skittles Bar",
                getShopGroupUpgradeCost(3, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.SKITTLES_BAR) + " Silver Upgrade\n\n" +
                        """
                       You hire another capable bouncer.
                       He seems to be a big anime fan and provides 
                       you not only with great moderation but also
                       bopping playlists and a huge anime catalogue.
                       This means you can start anime nights at your bar
                       and attract even more customers!
                       
                       +100% increase in skittles bar production""",
                "upgrades/skittles bar_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.CYBERPUNK_BAR,"Silver Cyberpunk Bar",
                getShopGroupUpgradeCost(6, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.CYBERPUNK_BAR) + " Silver Upgrade\n\n" +
                        """
                       You further expand your assortment of drinks.
                       You want to attract cat girls and appeal to their
                       tastes. Those cat girls have been created with
                       skittle nano machines technology and will help
                       bring more people to the bar. Mainly because of
                       their Stellar performances during karaoke nights.
                       
                       +100% increase in cyberpunk bar production""",
                "upgrades/cyberpunk bar_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.MOUNTAIN,"Silver Mountain",
                getShopGroupUpgradeCost(4, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.MOUNTAIN) + " Silver Upgrade\n\n" +
                        """
                        You keep training. You still have not reached your goal.
                        The summit needs to be reached faster and you say that
                        nothing is gonna stop you. 
                        And you are right. After learning new techniques and
                        even more training you achieve a time of
                        41m 23s 972ms!
                        
                        +100% increase in Mountain skittles production""",
                "upgrades/mountain_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.COLLAB,"Silver Collab",
                getShopGroupUpgradeCost(5, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.COLLAB) + " Silver Upgrade\n\n" +
                        """
                        You collab with a streamer that is very wholesome
                        most of the time but can also hammer very hard. uwu
                        You play some weird bunny game and have a great time
                        laughing together.
                                          
                        +100% increase in skittles from collabs""",
                "upgrades/collab_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ISEKAI,"Silver Isekai",
                getShopGroupUpgradeCost(7, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.ISEKAI) + " Silver Upgrade\n\n" +
                        """
                       In this Wonderful World you meet a splendid Crusader.
                       She seems to have strong defenses and insists on helping
                       you produce more skittles. Transporting all the skittles by
                       herself, she says it's fine to work her to the bone 24/7.
                       So you take her up on her offer 
                       and let her handle the isekai skittles.
                       
                       +100% increase in skittles from isekais""",
                "upgrades/isekai_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.TAILOR,"Silver Tailor",
                getShopGroupUpgradeCost(8, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.TAILOR) + " Silver Upgrade\n\n" +
                        """
                       Improve your COUNTER HITTO and GAADO BUREKO.
                       
                       +100% skittles from tailors""",
                "upgrades/tailor_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DRAGON,"Silver Dragon",
                getShopGroupUpgradeCost(9, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.DRAGON) + " Silver Upgrade\n\n" +
                        """
                        There is a smol dragon kid that possesses strong 
                        electric powers. Together with her you 
                        create an abomination of a skittle creature.
                        You decide to call it SkittleStein's monster,
                        as the dragon assumes that as a stage name.
                        
                        +100% increase in Dragon skittles production""",
                "upgrades/dragon_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DANCE_FLOOR,"Silver Dance Floor",
                getShopGroupUpgradeCost(10, 2), 2,
                shopgroupTypeToString(ShopGroup.Type.DANCE_FLOOR) + " Silver Upgrade\n\n" +
                        """
                        *padoruing intensifies even more*
                        
                        +100% increase in Dance Floor skittles production""",
                "upgrades/dance floor_silver.png"));

        //gold upgrades
        upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Gold Clicker",
                getShopGroupUpgradeCost(0, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.CLICKER) + " Gold Upgrade\n\n" +
                        """
                       The skittle smuggling has started a whole skittle mafia.
                       Ruthlessly stealing skittles, risking it all on every
                       heist. Especially dangerous are two infamous mafia bosses 
                       working together and stealing tons and tons of skittles.
                       The clicker crime syndicate is at its peak.
                       
                       +100% increase in Clicker skittles production""",
                "upgrades/clicker_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Gold Granny",
                getShopGroupUpgradeCost(1, 3), 2,
                testing ? "+100% grannies skittle production" :
                shopgroupTypeToString(ShopGroup.Type.GRANNY) + " Gold Upgrade\n\n" +
                        """
                       The grannies have founded a labour union to stand
                       against those \"ridiculous\" working conditions.
                       Now it takes mor than just one Sir to keep them at bay.
                       Luckily with this upgrade another knight joins 
                       the management. She is also wielding a sword,
                       making her arguments to work twice as hard pretty convincing.
                       
                       +100% grannies skittle production""",
                "upgrades/granny_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Gold Ducks",
                getShopGroupUpgradeCost(2, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.DUCK) + " Gold Upgrade\n\n" +"""
                       The leg regrowth of your duck has reached a point
                       that seems quite concerning. The workers are having 
                       trouble harvesting the skittles from its legs as fast
                       as it regrows them. This even makes the newest season
                       of Made in Abyss pale in comparison.
                       You may have qualms but it's for the greater good!
                       
                       +100% increase in skittles producktion""",
                "upgrades/duck_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Gold Skittles Bar",
                getShopGroupUpgradeCost(3, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.SKITTLES_BAR) + " Gold Upgrade\n\n" +
                        """
                       The numbers of customers in your bar are only growing
                       and growing. That calls for another bouncer.
                       And you really dont wanna piss this one off.
                       All the troublemakers get popped like they are mere 
                       balloons. This must be the technique of an expert
                       who has studied the arts of bloon popping for a long time.
                       Even more people join the bar 
                       and seem to enjoy the popping sounds.
                       
                       +100% increase in skittles bar production""",
                "upgrades/skittles bar_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.CYBERPUNK_BAR,"Gold Cyberpunk Bar",
                getShopGroupUpgradeCost(6, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.CYBERPUNK_BAR) + " Gold Upgrade\n\n" +
                        """
                       You and your boss work together to improve your 
                       assortment of drinks even more. She may seem very
                       carefree at first but she cares a lot about the bar
                       and the people working there.
                       Just dont cause any ruckus or she will literally crush
                       you with her mechanical arm.
                       
                       +100% increase in cyberpunk bar production""",
                "upgrades/cyberpunk bar_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.MOUNTAIN,"Gold Mountain",
                getShopGroupUpgradeCost(4, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.MOUNTAIN) + " Gold Upgrade\n\n" +
                        """
                        You are already fast as frick. But you still have one more goal.
                        You want to climb this mountain in under 40min.
                        After an insane amount of practice and some heartbreaking set backs
                        Your climb finally gets accepted into the official mountain climber 
                        fasttraversing.com leaderboards.
                        And what a climb it was! With a whopping 39m 00s 968ms, you not only
                        reach your goal, but shatter it completely by almost a whole minute.
                        Now that's an achievement.
                        
                        +100% increase in Mountain skittles production
                        Also unlocks golden Strawberrery animation""",
                "upgrades/mountain_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.COLLAB,"Gold Collab",
                getShopGroupUpgradeCost(5, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.COLLAB) + " Gold Upgrade\n\n" +
                        """
                        Mouse and flying mouse make a great duo.
                        And their combined Ara Aras are really something else.
                        With the combined powers of two japanese onee-san
                        nothing can stop you.
                                          
                        +100% increase in skittles from collabs""",
                "upgrades/collab_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ISEKAI,"Gold Isekai",
                getShopGroupUpgradeCost(7, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.ISEKAI) + " Gold Upgrade\n\n" +
                        """
                       You have finally found it. The ultimate magic.
                       Or rather a girl that can cast it. You join
                       hands with this powerful skittle arch mage
                       and together you cast the highest level magic spell
                       Multiple Explosions Greater Ultimate Maximum Inferno Nuke.
                       Or M.E.G.U.M.I.N for short.
                       
                       +100% increase in skittles from isekais""",
                "upgrades/isekai_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.TAILOR,"Gold Tailor",
                getShopGroupUpgradeCost(8, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.TAILOR) + " Gold Upgrade\n\n" +
                        """
                       Perfect your COUNTER HITTO and GAADO BUREKO.
                       
                       +100% skittles from tailors""",
                "upgrades/tailor_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DRAGON,"Gold Dragon",
                getShopGroupUpgradeCost(9, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.DRAGON) + " Gold Upgrade\n\n" +
                        """
                        Together with Tohru (the objectively best dragon)
                        you improve your dragon tail production by a lot!
                        Nothing beats maid dragons after all.
                        
                        +100% increase in Dragon skittles production""",
                "upgrades/dragon_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DANCE_FLOOR,"Gold Dance Floor",
                getShopGroupUpgradeCost(10, 3), 2,
                shopgroupTypeToString(ShopGroup.Type.DANCE_FLOOR) + " Gold Upgrade\n\n" +
                        """
                        *you padoru so hard, every day is now christmas*
                        
                        +100% increase in Dance Floor skittles production""",
                "upgrades/dance floor_gold.png"));

        this.goldenActive = false;
        this.barVisitorNames = new ArrayList<>();
        barVisitorNames.add("echo_gogo");
        barVisitorNames.add("peacemillian");
        barVisitorNames.add("sirpurrr");
        barVisitorNames.add("dr3adfulbear");
        barVisitorNames.add("alphatater");
        barVisitorNames.add("mokka_jm");
        barVisitorNames.add("mr__mongo");
        barVisitorNames.add("hispeedhit");
        barVisitorNames.add("bloodstaineddragon");
        barVisitorNames.add("kinkaikii");
        barVisitorNames.add("nonpareille");
        barVisitorNames.add("just_a_jen");
        barVisitorNames.add("mattyworth");
        barVisitorNames.add("strikestwice");
        barVisitorNames.add("hazymelon7");
        barVisitorNames.add("chicken_beer");
        barVisitorNames.add("nepunepuuu");
        barVisitorNames.add("chaincat");
        barVisitorNames.add("dashinka5632");
        barVisitorNames.add("jasons_wrong");
        barVisitorNames.add("solice55");
        barVisitorNames.add("domhugs");
        barVisitorNames.add("izori_");
        barVisitorNames.add("jud3_cryobyte");
        barVisitorNames.add("marinlicious");
        barVisitorNames.add("hoseoak");
        barVisitorNames.add("sevenjace");
        barVisitorNames.add("apeirocell");
        barVisitorNames.add("arzosah77");
        barVisitorNames.add("alwein");
        barVisitorNames.add("mastakazam");
        barVisitorNames.add("kasokuzo");
        barVisitorNames.add("aninsaneyandere");
        barVisitorNames.add("basicallya_neet");
        barVisitorNames.add("t3mulin");
        barVisitorNames.add("el_peluchini");
        barVisitorNames.add("ozzzybeef");
        barVisitorNames.add("gem_supern0va89");
        barVisitorNames.add("hardcoremethaddict");
        barVisitorNames.add("themasterofblubb");
        barVisitorNames.add("dromeivirpio");
        barVisitorNames.add("passi2612");
        barVisitorNames.add("malinalliy");
        barVisitorNames.add("fiachandraw");
        barVisitorNames.add("allozingy");
        barVisitorNames.add("jasperzev_vt");
        barVisitorNames.add("sakoneko");
        barVisitorNames.add("geneoloro");
        barVisitorNames.add("dimirinfiltrator");
        barVisitorNames.add("citrussushi");
        barVisitorNames.add("kaszkaaaa_");
        barVisitorNames.add("riderknight_");
        barVisitorNames.add("redsun_nl");
        barVisitorNames.add("thezikes");
        barVisitorNames.add("bratunor");
        barVisitorNames.add("razgrizthedemon");
        barVisitorNames.add("littlehizi");
        barVisitorNames.add("eavee");
        barVisitorNames.add("deanerrl");
        barVisitorNames.add("cipher1010");
        barVisitorNames.add("rilous");
        barVisitorNames.add("plururu_");
        barVisitorNames.add("fruitbatanyu");
        barVisitorNames.add("maylouuu");
        barVisitorNames.add("willowandbiscuit");
        barVisitorNames.add("simnoire");
        barVisitorNames.add("cinnabar");
        barVisitorNames.add("duelscreens");
        barVisitorNames.add("armazion");
        barVisitorNames.add("leydrewle");
        barVisitorNames.add("saltiestbun");
        barVisitorNames.add("kyrie_gamin");
        barVisitorNames.add("renerte");
        barVisitorNames.add("anoorah");
        barVisitorNames.add("creeperwaldo");
        barVisitorNames.add("prettyprincesspotato");
        barVisitorNames.add("clunia");
        barVisitorNames.add("inu_anzu");
        barVisitorNames.add("myuknekoshita");
        barVisitorNames.add("kimion");
        barVisitorNames.add("sir jude");
        barVisitorNames.add("saltiestofsquids");
        barVisitorNames.add("exodiac");
        barVisitorNames.add("raitaro");
        barVisitorNames.add("lewdcharizard");
        barVisitorNames.add("skyebison");
        barVisitorNames.add("sumage");
        barVisitorNames.add("akiba_illusion");
        barVisitorNames.add("monokoyumi");
        barVisitorNames.add("mrpockets150");
        barVisitorNames.add("harrajuku");
        barVisitorNames.add("xeirno");
        barVisitorNames.add("totouri");
        barVisitorNames.add("kingcobra");
        barVisitorNames.add("musicomp");
        barVisitorNames.add("moonsadow");
        System.out.println("number of names: " + barVisitorNames.size());
    }

    private long getShopGroupUpgradeCost(int shopgroupNumber, int upgradeNumber) {
        return Math.round(shopGroupSkittles[shopgroupNumber] * costMultipliers[shopgroupNumber]
                * Math.pow(upgradeCostMultiplier, (2*upgradeNumber) - 1)); //TODO figure out right pow, used to be upgradenumber^2
    }

    public void setupShop(List<Object> objects){
        setSkittles(objects.get(0) instanceof Long ? (Long) objects.get(0) : (Integer) objects.get(0));
//        shopGroups.get(0).setNumber(objects.get(0) instanceof Long ? (Long) objects.get(1) : (Integer) objects.get(1));
//        shopGroups.get(1).setNumber(objects.get(2) instanceof Long ? (Long) objects.get(2) : (Integer) objects.get(2));
//        shopGroups.get(2).setNumber(objects.get(3) instanceof Long ? (Long) objects.get(3) : (Integer) objects.get(3));
//        shopGroups.get(3).setNumber(objects.get(4) instanceof Long ? (Long) objects.get(4) : (Integer) objects.get(4));
        for (int i = 0; i < numberOfShopGroups(); i++) {
            shopGroups.get(i).setNumber(objects.get(i+1) instanceof Long ? (Long) objects.get(i+1) : (Integer) objects.get(i+1));
        }

        for (int i = 0; i < upgrades.size(); i++) {
            if ((boolean)objects.get(shopGroups.size()+1+i)){
                unlockUpgrade(i);
            }
        }
    }

    public int numberOfShopGroups (){
        return shopGroups.size();
    }

    public int numberOfUpgrades(){
        return upgrades.size();
    }

    public long getClickerNumber() {
        return shopGroups.get(0).getNumber();
    }

    public List<ShopGroup> getShopGroups() {
        return shopGroups;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Upgrade getUpgrade(int index) {
        return upgrades.get(index);
    }

    public long getSkittles() { //MAX signed long value about 5x10^15
        return skittles;
    }

    public long getSkittlesPerSecond(){
        long result = 0;
        for (ShopGroup s:
             shopGroups) {
            result += s.getSkittlesPerSecond();
        }
        return Math.round(result * generalModifier * (goldenActive ? goldenModifier : 1));
    }

    public void setSkittles(long skittles) {
        this.skittles = skittles;
    }
    public void updateSkittles() {
        skittles += getSkittlesPerSecond();
    }
    public void updateClickModifier(double modifier){
        clickModifier *= modifier;
    }
    public void updateGoldenModifier(double modifier){
        goldenModifier *= modifier;
    }

    public void updateMilkModifier(double modifier){
        milkModifier *= modifier;
    }
    public void updateGeneralModifier(double modifier){
        generalModifier *= modifier;
    }
    public void click() {
        skittles += getSkittlesPerClick();
        milkClicksMod+= milkClicksMod < MAX_MILK_CLICKS_MOD ? MILK_CLICKS_MOD_INCREMENT : 0;
    }

    public double getClickModifier(){
        return clickModifier;
    }

    public long getSkittlesPerClick(){
        return Math.round(baseSkittlesPerClick * clickModifier *  (1 + ((milkModifier-1) * milkClicksMod)) * generalModifier *(goldenActive ? goldenModifier : 1));
    }

    public void pay(long cost) {
        skittles -= cost;
    }

    public void unlockUpgrade(int index) {
        Upgrade upgrade = upgrades.get(index);
        upgrade.unlock();
        MilkState.changeState(index);
        ShopGroup.Type type = upgrade.getType();
        switch (type) {
            case PLAYER -> updateClickModifier(upgrade.getModifier());
            case GOLDEN -> updateGoldenModifier(upgrade.getModifier());
            case MILK -> updateMilkModifier(upgrade.getModifier());
            case ALL -> updateGeneralModifier(upgrade.getModifier());
            default -> {
                for (ShopGroup s : shopGroups) {
                    if (s.getType() == upgrade.getType()) {
                        s.updateModifier(upgrade.getModifier());
                        return;
                    }
                }
            }
        }
    }

    public void displayedUpgrade(int index){
        alreadyDisplayedUpgrades.add(index);
    }

    public int[] getNewUpgradeIndex() {
        int[] indices = new int[5];
        for (int i= 0; i < indices.length; i++) {
            indices[i] = -1;
        }
        int index = 0;
        for (int i = 0; i < upgrades.size(); i++) {
            if (index == 5) return indices;
            if (!alreadyDisplayedUpgrades.contains(i) && upgrades.get(i).isVisible()) {
                indices[index] = i;
                index++;
            }
        }
        return indices;
    }

    public List<Integer> getAlreadyDisplayedUpgrades() {
        return alreadyDisplayedUpgrades;
    }

    public void goldenActive(boolean b) {
        goldenActive = b;
    }
    public String shopgroupTypeToString(ShopGroup.Type type){
        String s = type.name().charAt(0) + type.name().toLowerCase(Locale.ROOT).substring(1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_'){
                s = s.substring(0, i) + " " + s.substring(i+1, i+2).toUpperCase(Locale.ROOT) + s.substring(i+2);
            }
        }
        return s;
    }

    public void milkClickTimeDecrement(){
        milkClicksMod -= milkClicksMod > 0.1 ? 0.1 : 0;
    }
    public double getMilkClicksMod() {
        return milkClicksMod;
    }
    public double getMilkClicksPercent() {
        return milkClicksMod/MAX_MILK_CLICKS_MOD;
    }

    public void updateVisibleUpgrades() {
        for (Upgrade u:
             upgrades) {
            ShopGroup.Type type = u.getType();
            if (skittles >= u.getCost() * updateVisibleFraction){
                if (type == ShopGroup.Type.ALL || type == ShopGroup.Type.GOLDEN
                        || type == ShopGroup.Type.MILK || type == ShopGroup.Type.PLAYER) {
                    u.makeVisible();
                } else if (shopGroups.get(type.ordinal() - 4).getNumber() > 0){
                    u.makeVisible();
                }
            }
        }
    }

    public void enableTestMode() {
        skittles += 9999999999999L;
    }

    public boolean isAllUnlocked() {
        for (ShopGroup s:
             shopGroups) {
            if (s.getNumber() < s.getMAX_NUMBER()) return false;
        }
        for (Upgrade u:
             upgrades) {
            if (!u.getName().equals("Finale")){
                if (!u.isUnlocked()) return false;
            }
        }
        return true;
    }
    public Upgrade getUpgradeByName(String name){
        for (Upgrade u:
        upgrades) {
            if (u.getName().equals(name)) return u;
        }
        return null;
    }
    public String getBarVisitorName(int index){
        return barVisitorNames.get(index);
    }
}