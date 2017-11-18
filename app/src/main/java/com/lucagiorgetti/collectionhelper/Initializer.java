package com.lucagiorgetti.collectionhelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lucagiorgetti.collectionhelper.model.Categories;
import com.lucagiorgetti.collectionhelper.model.Colors;
import com.lucagiorgetti.collectionhelper.model.ExtraLocales;
import com.lucagiorgetti.collectionhelper.model.Producer;
import com.lucagiorgetti.collectionhelper.model.Set;
import com.lucagiorgetti.collectionhelper.model.Surprise;
import com.lucagiorgetti.collectionhelper.model.User;
import com.lucagiorgetti.collectionhelper.model.Year;

import java.util.Locale;

/**
 * Created by Luca on 06/11/2017.
 */

public class Initializer {
    FirebaseDatabase database = DatabaseUtility.getDatabase();
    DatabaseReference surprises = database.getReference("surprises");
    DatabaseReference sets = database.getReference("sets");
    DatabaseReference missings = database.getReference("missings");
    DatabaseReference producers = database.getReference("producers");
    DatabaseReference years = database.getReference("years");
    User user = null;

    public Initializer(User currentUser){
        this.user = currentUser;
    }

    public void insertData(){
        Producer kinderSorpresa = new Producer("Kinder", "Sorpresa", 1, Colors.LIGHT_BLUE);
        insertProducer(kinderSorpresa);
        Producer kinderMerendero = new Producer("Kinder", "Merendero", 2, Colors.ORANGE);
        insertProducer(kinderMerendero);

        Year kinSorp2017 = new Year(2017, kinderSorpresa);
        insertYear(kinSorp2017);
        Year kinMer2017 = new Year(2017, kinderMerendero);
        insertYear(kinMer2017);

        //region CattivissimoMe3
        Set catMe3 = new Set("Cattivissimo Me 3", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FBPZ_CattivissimoMe3_2017.jpg?alt=media&token=4b9069ac-cf8a-4a2d-b7d4-6780e61eb89a", Colors.LIGHT_BLUE, Categories.HANDPAINTED);

        Surprise SD697 = new Surprise("Gru", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD697.jpg?alt=media&token=5e9e86c0-c965-44d0-b3a0-cb70dc0e8dbf", "SD697", catMe3);
        Surprise SD698 = new Surprise("Agnes", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD698.jpg?alt=media&token=adb8a3a0-4a53-4e3b-8869-205b5e279589", "SD698", catMe3);
        Surprise SD699 = new Surprise("Edith", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD699.jpg?alt=media&token=5bbb69ff-3b81-4ac3-ab73-8a76b4828fad", "SD699", catMe3);
        Surprise SD700 = new Surprise("Stuart", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD700.jpg?alt=media&token=ce4c3817-8d80-4ec4-b7fd-590a7d12d9dd", "SD700", catMe3);
        Surprise SD707 = new Surprise("Margo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD707.jpg?alt=media&token=1735780c-9d87-4125-8c1f-23e4fa95ad08", "SD707", catMe3);
        Surprise SD744 = new Surprise("Dave", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD744.jpg?alt=media&token=7cb6f90c-a8a6-486a-bd2d-ac87015af2e5", "SD744", catMe3);
        Surprise SD745 = new Surprise("Phil", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD745.jpg?alt=media&token=a15d0f0f-764e-4f6c-82ee-daf38e43b22d", "SD745", catMe3);
        Surprise SD746 = new Surprise("Balthazar Bratt", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD746.jpg?alt=media&token=5167cbe2-bfca-49b4-a199-a17b523cf52f", "SD746", catMe3);
        Surprise SD747 = new Surprise("Carl", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD747.jpg?alt=media&token=0f4de21b-743b-42e2-8761-c55addcbc199", "SD747", catMe3);
        Surprise SD748A = new Surprise("Dru", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FCattivissimoMe3%2FSD748.jpg?alt=media&token=c98272c0-9992-4ddd-9430-ab73e56eb561", "SD748A", catMe3);

        insertSet(catMe3);
        insertSurprise(SD697);
        insertSurprise(SD698);
        insertSurprise(SD699);
        insertSurprise(SD700);
        insertSurprise(SD707);
        insertSurprise(SD744);
        insertSurprise(SD745);
        insertSurprise(SD746);
        insertSurprise(SD747);
        insertSurprise(SD748A);
        //endregion

        //region Natale
        Set natale = new Set("Natale", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FBPZ_Natale_2017.jpg?alt=media&token=ec51d752-ab7e-4f35-a6cc-ba412c075e0a", Colors.LIGHT_BLUE, Categories.HANDPAINTED);

        Surprise SE269 = new Surprise("Orso Polare", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD269.jpg?alt=media&token=d81f00bc-8561-4038-8bae-0e03a4ed9c32", "SE269", natale);
        Surprise SE270 = new Surprise("Renna", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD270.jpg?alt=media&token=e9a14865-7afb-4811-bff6-70dfe0d1ad5c", "SE270", natale);
        Surprise SE271 = new Surprise("Volpe", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD271.jpg?alt=media&token=240bd603-61e2-4b53-801f-26c2b04c98e4", "SE271", natale);
        Surprise SE272 = new Surprise("Lupo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD272.jpg?alt=media&token=a9566d03-0ffe-45a5-b50f-565f67d0da01", "SE272", natale);
        Surprise SE273 = new Surprise("Gufo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD273.jpg?alt=media&token=5869c460-1635-4968-b3b8-8ef5b35f3e6b", "SE273", natale);
        Surprise SE274 = new Surprise("Pinguino", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD274.jpg?alt=media&token=974208a8-c5e4-4c05-b62b-00c88fd124fc", "SE274", natale);
        Surprise SE275 = new Surprise("Lepre", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD275.jpg?alt=media&token=5632a43e-93c0-4bd8-94f6-779ce10ad715", "SE275", natale);
        Surprise SE276 = new Surprise("Babbo Natale", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FNatale%2FSD276.jpg?alt=media&token=7b941204-0db3-4a51-9dbb-8b7333ac5c2c", "SE276", natale);

        insertSet(natale);
        insertSurprise(SE269);
        insertSurprise(SE270);
        insertSurprise(SE271);
        insertSurprise(SE272);
        insertSurprise(SE273);
        insertSurprise(SE274);
        insertSurprise(SE275);
        insertSurprise(SE276);
        //endregion

        //region DoraEsploratrice
        Set dora = new Set("Dora l'esploratrice", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FBPZ_Dora_2017.jpg?alt=media&token=42a23a43-e8c4-49f6-9223-145c33b94665", Colors.LIGHT_BLUE, Categories.HANDPAINTED);

        Surprise SE332 = new Surprise("Boots", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FSE332.jpg?alt=media&token=45c35463-15c3-4283-a485-2e822983fc56", "SE332", dora);
        Surprise SE333 = new Surprise("Diego Marquez", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FSE333.jpg?alt=media&token=3cfa4253-2b39-4f40-8365-2a9791523557", "SE333", dora);
        Surprise SE334 = new Surprise("Dora", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FSE334.jpg?alt=media&token=d91ee3d1-2787-482e-b3a7-9ce2ad7e25dc", "SE334", dora);
        Surprise SE335= new Surprise("Dora con fiore", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FSE335.jpg?alt=media&token=8b1ad419-bb58-453f-a5e2-ef0286b96934", "SE335", dora);
        Surprise SE336= new Surprise("Swiper", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FSE336.jpg?alt=media&token=429d70f9-7930-4afc-8f07-5fb9c3abf1b2", "SE336", dora);
        Surprise SE337 = new Surprise("Tyco", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FDora%2FSE337.jpg?alt=media&token=7ed7e37e-3c7c-4432-8bfc-cf57d249066a", "SE337", dora);

        insertSet(dora);
        insertSurprise(SE332);
        insertSurprise(SE333);
        insertSurprise(SE334);
        insertSurprise(SE335);
        insertSurprise(SE336);
        insertSurprise(SE337);
        //endregion

        //region SuperHeroGirls
        Set supher = new Set("Super Hero Girls", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FBPZ_SuperHeroGirls_2017.jpg?alt=media&token=ad13b0d9-5e4f-4030-bd7e-834142e5121d", Colors.LIGHT_BLUE, Categories.HANDPAINTED);

        Surprise SE268 = new Surprise("Bumblebee", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE268.jpg?alt=media&token=8ede04ee-a96c-48b3-bc29-b91087e5cf51", "SE268", supher);
        Surprise SE277 = new Surprise("Catwomen", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE277.jpg?alt=media&token=dba10b67-3a2e-4eaa-86ff-b09d54536acb", "SE277", supher);
        Surprise SE278 = new Surprise("Poison Ivy", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE278.jpg?alt=media&token=4b5880b6-23e7-46f0-97b6-295423d095b5", "SE278", supher);
        Surprise SE279 = new Surprise("Supergirl", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE279.jpg?alt=media&token=e4e12b06-f63b-4e3c-b6c3-f0ae8e1c763a", "SE279", supher);
        Surprise SE280 = new Surprise("Wonder Woman", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE280.jpg?alt=media&token=3d5652aa-94e3-44de-8123-a3c14b22fcd7", "SE280", supher);
        Surprise SE287 = new Surprise("Harley Quinn", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE287.jpg?alt=media&token=b1f2abfd-980c-4295-bdfa-f83146fbdef9", "SE287", supher);
        Surprise SE288 = new Surprise("Katana", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSuperHeroGirls%2FSE288.jpg?alt=media&token=f7601617-8645-4ad9-85bf-d963302ca4ed", "SE288", supher);

        insertSet(supher);
        insertSurprise(SE268);
        insertSurprise(SE277);
        insertSurprise(SE278);
        insertSurprise(SE279);
        insertSurprise(SE280);
        insertSurprise(SE287);
        insertSurprise(SE288);
        //endregion

        //region BraccialettiSuperHeroGirl
        Set bracsup = new Set("Braccialetti Super Hero Girl", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBraccialettiSuperHeroGirls%2FBPZ_AccessoriSuperHeroGirl_2017.jpg?alt=media&token=8b502d56-e15b-4ae0-bc0e-efb7f8077530", Colors.LIGHT_BLUE, Categories.HANDPAINTED);

        Surprise SE359 = new Surprise("Harley Quinn", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBraccialettiSuperHeroGirls%2FSE359.jpg?alt=media&token=6daf7143-7e7b-455a-99c5-1a7262583727", "SE359", bracsup);
        Surprise SE360 = new Surprise("Katana", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBraccialettiSuperHeroGirls%2FSE360.jpg?alt=media&token=9ac0c8dd-bb93-4bd5-9f40-a827aff41bdd", "SE360", bracsup);
        Surprise SE750 = new Surprise("Batgirl", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBraccialettiSuperHeroGirls%2FSE750.jpg?alt=media&token=bf492e10-6768-4b0f-bb91-d6f56d8f7985", "SE750", bracsup);
        Surprise SE751 = new Surprise("Wonder Woman", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBraccialettiSuperHeroGirls%2FSE751.jpg?alt=media&token=0fc9f9b8-0059-4f66-b089-6795ad540511", "SE751", bracsup);

        insertSet(bracsup);
        insertSurprise(SE359);
        insertSurprise(SE360);
        insertSurprise(SE750);
        insertSurprise(SE751);
        //endregion

        //region AccessoriCattivissimoMe3
        Set acccatt = new Set("Accessori Cattivissimo Me 3", kinMer2017, kinderMerendero, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FBPZ_AccessoriCattivissimoMe_2017.jpg?alt=media&token=55dc61dc-b37b-421c-87d1-7eff9a381eef", Colors.LIGHT_BLUE, Categories.COMPO);

        Surprise SE528B = new Surprise("Frisbee", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FSE528B.jpg?alt=media&token=28e96ea0-4c1d-415b-8e3d-d0537b36b921", "SE528B", acccatt);
        Surprise SE552B = new Surprise("Componi Minions", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FSE552B.jpg?alt=media&token=4be0b73f-8871-4bba-a71e-985aa7a9a335", "SE552B", acccatt);
        Surprise SE769 = new Surprise("Figurine", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FSE769.jpg?alt=media&token=641e9154-c887-4967-a849-d23a3421b171", "SE769", acccatt);
        Surprise SE770 = new Surprise("Figurine con labirinto", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FSE770.jpg?alt=media&token=3117a15d-202a-4f99-94b8-390a44f856d5", "SE770", acccatt);
        Surprise SE771 = new Surprise("Trottola Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FSE771.jpg?alt=media&token=b249ecdc-58fb-4c68-be06-78d570acaa73", "SE771", acccatt);
        Surprise SE772 = new Surprise("Trottola Rossa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Merendero%2F2017%2FAccessoCattivissimoMe3%2FSE772.jpg?alt=media&token=51d6536a-bbe0-4d66-bb90-9f25fda883d0", "SE772", acccatt);

        insertSet(acccatt);
        insertSurprise(SE528B);
        insertSurprise(SE552B);
        insertSurprise(SE769);
        insertSurprise(SE770);
        insertSurprise(SE771);
        insertSurprise(SE772);

        //endregion

        //region Farfalle
        Set farf = new Set("Farfalle", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Farfalle_2017.jpg?alt=media&token=731610d2-0990-45c4-beaa-6eb1e3fd43ef", Colors.PURPLE, Categories.COMPO);

        Surprise SD040D = new Surprise("Farfalla Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD040D.jpg?alt=media&token=79ef5019-95dc-482b-9bc3-9829f98fe9f3", "SD040D", farf);
        Surprise SD040E = new Surprise("Farfalla Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD040E.jpg?alt=media&token=e1597e35-041a-4469-b737-6ba5a88af8c8", "SD040E", farf);
        Surprise SD040F = new Surprise("Farfalla Azzurra", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD040F.jpg?alt=media&token=43494432-776e-43a6-8902-07ed7d207d6f", "SD040F", farf);

        insertSet(farf);
        insertSurprise(SD040D);
        insertSurprise(SD040E);
        insertSurprise(SD040F);
        //endregion

        //region AutoInPista
        Set autpis = new Set("Auto in pista", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AutoInPista_2017.jpg?alt=media&token=12dfac6c-0aec-4d65-97cf-e637ad3c3ad0", Colors.RED, Categories.COMPO);

        Surprise SD262A = new Surprise("Auto Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD262A.jpg?alt=media&token=69a7eb41-2821-45e6-85a8-7c99875a9993", "SD262A", autpis);
        Surprise SD263A = new Surprise("Auto Rossa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD263A.jpg?alt=media&token=5e682810-5f0b-4965-a266-9cee0f470e00", "SD263A", autpis);
        Surprise SD264A = new Surprise("Auto Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD264A.jpg?alt=media&token=45c94bfa-6c86-4c8b-9400-41d185323354", "SD264A", autpis);
        Surprise SD265A = new Surprise("Auto Nera", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD265A.jpg?alt=media&token=f93e7d66-d44d-4d44-b720-1a4f070ef867", "SD265A", autpis);
        Surprise SD266A = new Surprise("Auto Azzurra", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD266A.jpg?alt=media&token=36c916ea-99d3-4a62-a86e-cc92ac2b4c98", "SD266A", autpis);
        Surprise SD267A = new Surprise("Auto Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSD267A.jpg?alt=media&token=24e4108e-d99f-47e9-a0f4-eee602287ffd", "SD267A", autpis);

        insertSet(autpis);
        insertSurprise(SD262A);
        insertSurprise(SD263A);
        insertSurprise(SD264A);
        insertSurprise(SD265A);
        insertSurprise(SD266A);
        insertSurprise(SD267A);

        //endregion

        //region CuccioliDelBosco
        Set cucbos = new Set("Cuccioli Del Bosco", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_CuccioliDelBosco_2017.jpg?alt=media&token=ec5981f6-da75-4c98-8e04-5a5604be43a4", Colors.GREEN, Categories.COMPO);

        Surprise SE001 = new Surprise("Orso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE001.jpg?alt=media&token=9df27ac9-89fc-4977-9881-55c1737f9f94", "SE001", cucbos);
        Surprise SE002 = new Surprise("Coniglio", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE002.jpg?alt=media&token=af1e8197-34d3-48e4-a016-67b94dc30a47", "SE002", cucbos);
        Surprise SE003 = new Surprise("Lupo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE003.jpg?alt=media&token=790ee28d-cc20-4667-a85b-c4e78b02c27c", "SE003", cucbos);

        insertSet(cucbos);
        insertSurprise(SE001);
        insertSurprise(SE002);
        insertSurprise(SE003);
        //endregion

        //region CiondoliConAnelli
        Set cionanell = new Set("Ciondoli con anelli", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_CiondoliConAnelli_2017.jpg?alt=media&token=e23a1eb8-18c2-40f2-8a88-b8dc7920ab91", Colors.PURPLE, Categories.COMPO);

        Surprise SE019 = new Surprise("Anello Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE019.jpg?alt=media&token=2a76df00-4a34-4d07-9767-edd49c7705dc", "SE019", cionanell);
        Surprise SE020 = new Surprise("Anello Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE020.jpg?alt=media&token=16cf4d84-6f98-4b29-8a8a-116684927c52", "SE020", cionanell);
        Surprise SE021 = new Surprise("Anello Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE021.jpg?alt=media&token=0922edbc-0284-4048-8c6c-b71bf625c7a7", "SE021", cionanell);

        insertSet(cionanell);
        insertSurprise(SE019);
        insertSurprise(SE020);
        insertSurprise(SE021);
        //endregion

        //region AutoVolanti
        Set autvol = new Set("Auto volanti", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_MacchineVolanti_2017.jpg?alt=media&token=e8094d93-76ff-43d9-9b84-e52735f6f980", Colors.RED, Categories.COMPO);

        Surprise SE034 = new Surprise("Auto Verde Acqua", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE034.jpg?alt=media&token=56927c91-ec2b-4cb6-a421-7c9a315f3fe7", "SE034", autvol);
        Surprise SE035 = new Surprise("Auto Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE035.jpg?alt=media&token=ff824123-129d-49d5-be08-4f2b08c0497b", "SE035", autvol);
        Surprise SE036 = new Surprise("Auto Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE036.jpg?alt=media&token=65fc2dd2-a1cb-4cdb-9178-99f8bfa7ca37", "SE036", autvol);
        Surprise SE037 = new Surprise("Auto Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE037.jpg?alt=media&token=ca1bccb4-4b4d-4f20-87c5-0eae192216f3", "SE037", autvol);

        insertSet(autvol);
        insertSurprise(SE034);
        insertSurprise(SE035);
        insertSurprise(SE036);
        insertSurprise(SE037);
        //endregion

        //region Fate
        Set fate = new Set("Fate", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Fate_2017.jpg?alt=media&token=caec2a2c-203f-4126-be56-bc760908668f", Colors.PURPLE, Categories.COMPO);

        Surprise SE042 = new Surprise("Fata Azzurra", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE042.jpg?alt=media&token=dba162ff-91a2-4a11-8121-bde4635834ff", "SE042", fate);
        Surprise SE043 = new Surprise("Fata Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE043.jpg?alt=media&token=80cf29b3-613e-4057-a973-d74f43554e9f", "SE043", fate);
        Surprise SE044 = new Surprise("Fata Marrone", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE044.jpg?alt=media&token=654dc54b-18ee-4dec-b6b7-3b7777bab1e8", "SE044", fate);
        Surprise SE045 = new Surprise("Fata Verde Acqua", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE045.jpg?alt=media&token=f2d5d414-2253-4f36-bf7f-0a052ad03b14", "SE045", fate);

        insertSet(fate);
        insertSurprise(SE042);
        insertSurprise(SE043);
        insertSurprise(SE044);
        insertSurprise(SE045);
        //endregion

        //region AnelliDelleFate
        Set anellifate = new Set("Anelli delle Fate", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AnelliDelleFate_2017.jpg?alt=media&token=73d9852f-53e2-4baa-81ae-50f1a4fd7485", Colors.PURPLE, Categories.COMPO);

        Surprise SE048 = new Surprise("Anello Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE048.jpg?alt=media&token=82ebbf4f-1bf9-4f2e-967e-0de27e7ea736", "SE048", anellifate);
        Surprise SE110 = new Surprise("Anello Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE110.jpg?alt=media&token=586775c5-cc65-435d-bb65-d4b5e6b362f7", "SE110", anellifate);
        Surprise SE111 = new Surprise("Anello Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE111.jpg?alt=media&token=890c0302-bb6f-4457-ac51-2da3f5ed3162", "SE111", anellifate);

        insertSet(anellifate);
        insertSurprise(SE048);
        insertSurprise(SE110);
        insertSurprise(SE111);
        //endregion

        //region AutoGirevoli
        Set autgir = new Set("Auto girevoli", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AutoGirevoli_2017.jpg?alt=media&token=f9bcca4c-924d-4529-8e7f-7d1ea816e255", Colors.RED, Categories.COMPO);

        Surprise SE050 = new Surprise("Auto Arancione/Bianca", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE050.jpg?alt=media&token=2ec1f6a9-2bc3-406f-8e5e-4689576bf944", "SE050", autgir);
        Surprise SE229 = new Surprise("Auto Arancione/Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE229.jpg?alt=media&token=8fbd31e8-0ee7-41c0-8ed0-ddd7b3f731e3", "SE229", autgir);
        Surprise SE230 = new Surprise("Auto Rossa/Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE230.jpg?alt=media&token=6a4699d1-89b1-4d37-bea2-c3a94f88b442", "SE230", autgir);
        Surprise SE231 = new Surprise("Auto Verde/Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE231.jpg?alt=media&token=131f7daa-80de-47fb-afa0-199b0c9c764f", "SE231", autgir);

        insertSet(autgir);
        insertSurprise(SE050);
        insertSurprise(SE229);
        insertSurprise(SE230);
        insertSurprise(SE231);
        //endregion

        //region Moto
        Set moto = new Set("Moto", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Moto_2017.jpg?alt=media&token=5f1489be-c4e0-420b-b3f8-bb78b9a561cf", Colors.BLUE, Categories.COMPO);

        Surprise SE051 = new Surprise("Moto Verde/Azzurra", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE051.jpg?alt=media&token=9e1fd2dd-7464-4c84-87d8-18c2c83d0086", "SE051", moto);
        Surprise SE052 = new Surprise("Moto Arancione/Nera", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE052.jpg?alt=media&token=a0534fbb-0e2e-4e11-bf78-0f23a76b6c7e", "SE052", moto);
        Surprise SE053 = new Surprise("Moto Azzurra/Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE053.jpg?alt=media&token=24fbc512-1a43-44e9-ac69-b4aaa152a518", "SE053", moto);
        Surprise SE054 = new Surprise("Moto Rossa/Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE054.jpg?alt=media&token=9c984700-0ef4-4bbc-a155-adf5d9111158", "SE054", moto);

        insertSet(moto);
        insertSurprise(SE051);
        insertSurprise(SE052);
        insertSurprise(SE053);
        insertSurprise(SE054);
        //endregion

        //region Felini
        Set felini = new Set("Felini", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Felini_2017.jpg?alt=media&token=a470370f-2a45-468f-bd7d-cb3bfa8b1afc", Colors.GREEN, Categories.COMPO);

        Surprise SE056 = new Surprise("Pantera", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE056.jpg?alt=media&token=43ac442c-91b2-45f7-909b-ade3103ee50f", "SE056", felini);
        Surprise SE173 = new Surprise("Leopardo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE173.jpg?alt=media&token=07922164-db30-4c6a-b01c-b817a6d2cb09", "SE173", felini);
        Surprise SE174 = new Surprise("Leone", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE174.jpg?alt=media&token=643cb26c-f80e-4cc7-80b6-bbe7205f0ad2", "SE174", felini);
        Surprise SE521 = new Surprise("Tigre", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE521.jpg?alt=media&token=b478fdbc-f1fe-4a5f-be4b-875a77dd8850", "SE521", felini);

        insertSet(felini);
        insertSurprise(SE056);
        insertSurprise(SE173);
        insertSurprise(SE174);
        insertSurprise(SE521);
        //endregion

        //region AutoMostrose
        Set autmost = new Set("Auto mostrose", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Auto%20Mostruose_2017.jpg?alt=media&token=a05a2c6b-fb3a-43d2-9963-0df289c920fc", Colors.RED, Categories.COMPO);

        Surprise SE059 = new Surprise("Auto Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE059.jpg?alt=media&token=465fbd2e-c9e2-4f73-bf37-5fab1c6b039c", "SE059", autmost);
        Surprise SE060 = new Surprise("Auto Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE060.jpg?alt=media&token=259a8d1f-238b-4cf5-8ae6-67c54819502f", "SE060", autmost);
        Surprise SE119 = new Surprise("Auto Rossa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE119.jpg?alt=media&token=f17168bd-38b3-4c24-8e8c-9b3694305c41", "SE119", autmost);
        Surprise SE120 = new Surprise("Auto Verde Acqua", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE120.jpg?alt=media&token=f2e6213f-29e8-44b6-8522-d1c626dbb509", "SE120", autmost);

        insertSet(autmost);
        insertSurprise(SE059);
        insertSurprise(SE060);
        insertSurprise(SE119);
        insertSurprise(SE120);
        //endregion

        //region AmiciAnimali
        Set amiani = new Set("Amici animali", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AmiciAnimali_2017.jpg?alt=media&token=86ac3e08-91a3-40c2-af72-55c7601715d7", Colors.GREEN, Categories.COMPO);

        Surprise SE069 = new Surprise("Tartaruga/Riccio", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE069.jpg?alt=media&token=82072a33-06b6-4e95-b585-952ba148911d", "SE069", amiani);
        Surprise SE152 = new Surprise("Pesci", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE152.jpg?alt=media&token=e6764859-6a3d-4e52-a906-c084b2d0c8fe", "SE152", amiani);
        Surprise SE153 = new Surprise("Maiale/Pecora", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE153.jpg?alt=media&token=d66e03f8-eda4-499d-b340-24668220d591", "SE153", amiani);
        Surprise SE154 = new Surprise("Squalo/Balena", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE154.jpg?alt=media&token=ec7dce10-e3c1-4983-86c6-24e971552359", "SE154", amiani);

        insertSet(amiani);
        insertSurprise(SE069);
        insertSurprise(SE152);
        insertSurprise(SE153);
        insertSurprise(SE154);
        //endregion

        //region Dinosauri
        Set dinos = new Set("Dinosauri", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Dinosauri_2017.jpg?alt=media&token=a5a45f62-b1f8-485c-8d89-1f4b36523480", Colors.GREEN, Categories.COMPO);

        Surprise SE079 = new Surprise("Dinosauro Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE079.jpg?alt=media&token=95d636ae-0056-4335-82c7-f0e1427a081c", "SE079", dinos);
        Surprise SE116 = new Surprise("Dinosauro Verde Acqua", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE116.jpg?alt=media&token=4edb9b43-762a-4387-b36e-6eb7f82a011a", "SE116", dinos);
        Surprise SE117 = new Surprise("Dinosauro Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE117.jpg?alt=media&token=6496d509-4f5a-4c3d-a63a-c81b6c9e5882", "SE117", dinos);
        Surprise SE118 = new Surprise("Dinosauro Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE118.jpg?alt=media&token=8128397e-6fd5-449a-8dca-e6eb0e7f5b80", "SE118", dinos);

        insertSet(dinos);
        insertSurprise(SE079);
        insertSurprise(SE116);
        insertSurprise(SE117);
        insertSurprise(SE118);
        //endregion

        //region TrottoleGambeLunghe
        Set trotgamb = new Set("Trottole gambe lunghe", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_TrottoleGambeLunghe_2017.jpg?alt=media&token=9654b56e-12a8-4825-bfb1-3aae101acd97", Colors.BLUE, Categories.COMPO);

        Surprise SE088 = new Surprise("Trottola Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE088.jpg?alt=media&token=7e84fbf8-a7f0-48d3-af34-fdcf534603e2", "SE088", trotgamb);
        Surprise SE089 = new Surprise("Trottola Azzurra", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE089.jpg?alt=media&token=533c094c-8310-4ba1-a9fb-d315f667c75e", "SE089", trotgamb);
        Surprise SE106 = new Surprise("Trottola Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE106.jpg?alt=media&token=a88c8eee-36bb-46da-a494-81c3e648428e", "SE106", trotgamb);
        Surprise SE107 = new Surprise("Trottola Rossa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE107.jpg?alt=media&token=1b2f10cb-22ff-4b76-aeeb-13938a5df017", "SE107", trotgamb);

        insertSet(trotgamb);
        insertSurprise(SE088);
        insertSurprise(SE089);
        insertSurprise(SE106);
        insertSurprise(SE107);
        //endregion

        //region Cestini
        Set cest = new Set("Cestini", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_CestiniDellaDifferenziata_2017.jpg?alt=media&token=40695841-d946-436b-a5f5-fbe19129a532", Colors.ORANGE, Categories.COMPO);

        Surprise SE094 = new Surprise("Cestino Azzurro", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE094.jpg?alt=media&token=01d83b21-bc29-4a4a-94f6-c1b7fd0f07ec", "SE094", cest);
        Surprise SE094A = new Surprise("Cestino Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE094A.jpg?alt=media&token=27065528-105a-4cb5-a898-4657e76fc034", "SE094A", cest);
        Surprise SE094B = new Surprise("Cestino Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE094B.jpg?alt=media&token=7a448185-f78e-4793-bd83-45d694b9761a", "SE094B", cest);
        Surprise SE094C = new Surprise("Cestino Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE094C.jpg?alt=media&token=d7cc061e-f659-42e1-b03f-7766eaf42b82", "SE094C", cest);

        insertSet(cest);
        insertSurprise(SE094);
        insertSurprise(SE094A);
        insertSurprise(SE094B);
        insertSurprise(SE094C);
        //endregion

        //region MezziDiTrasporto
        Set mezzi = new Set("Mezzi di trasporto", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_MezziDiTrasporto_2017.jpg?alt=media&token=4e359a02-6f3e-45af-95f0-2a47c743181a", Colors.RED, Categories.COMPO);

        Surprise SE098A = new Surprise("Camion Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE098A.jpg?alt=media&token=4774eb50-a953-4eeb-bf46-6ee7cc46352f", "SE098A", mezzi);
        Surprise SE099 = new Surprise("Camion Azzurro", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE099.jpg?alt=media&token=5aa3f292-8970-4777-aa1f-d3a8b08596ad", "SE099", mezzi);
        Surprise SE101A = new Surprise("Camion Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE101A.jpg?alt=media&token=3eeac441-a613-4280-8887-2cc5848597e1", "SE101A", mezzi);

        insertSet(mezzi);
        insertSurprise(SE098A);
        insertSurprise(SE099);
        insertSurprise(SE101A);
        //endregion

        //region FamiglieArtiche
        Set famart = new Set("Famiglie artiche", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_FamiglieArtiche_2017.jpg?alt=media&token=b7135e2a-a259-4af3-90fb-cf2d5f7d93df", Colors.GREEN, Categories.COMPO);

        Surprise SE102 = new Surprise("Renne", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE102.jpg?alt=media&token=8d30f4f9-be67-44db-bcaf-ac1abe654870", "SE102", famart);
        Surprise SE121 = new Surprise("Orsi", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE121.jpg?alt=media&token=844de5e0-2c6f-4fcc-9209-3548cb8d140f", "SE121", famart);
        Surprise SE122 = new Surprise("Pinguini", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE122.jpg?alt=media&token=9490f242-444f-4fdd-ac64-6f2ca02e9d1e", "SE122", famart);

        insertSet(famart);
        insertSurprise(SE102);
        insertSurprise(SE121);
        insertSurprise(SE122);
        //endregion

        //region TrottoleConLanciatore
        Set trottlan = new Set("Trottole con lanciatore", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_TrottoleConLanciatore_2017.jpg?alt=media&token=a14a968a-ecae-4828-a781-a8c9296c5a3a", Colors.BLUE, Categories.COMPO);

        Surprise SE108 = new Surprise("Trottola Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE108.jpg?alt=media&token=2f314c79-8d0c-40ef-9bec-9dd8646ad611", "SE108", trottlan);
        Surprise SE108A = new Surprise("Trottola Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE108A.jpg?alt=media&token=dd9c8f2d-7a2b-42a7-805d-bf5a6a2f04a1", "SE108A", trottlan);
        Surprise SE109 = new Surprise("Trottola Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE109.jpg?alt=media&token=9adb21c0-7b0e-41bf-8aa9-a18280a9f88d", "SE109", trottlan);
        Surprise SE109A = new Surprise("Trottola Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE109A.jpg?alt=media&token=ffbe70eb-1cb4-492f-88b2-f90719d1421e", "SE109A", trottlan);

        insertSet(trottlan);
        insertSurprise(SE108);
        insertSurprise(SE108A);
        insertSurprise(SE109);
        insertSurprise(SE109A);
        //endregion

        //region CuccioliDellaSavana
        Set cuccsav = new Set("Cuccioli della savana", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_CuccioliDellaSavana_2017.jpg?alt=media&token=66468b75-8ec6-45c7-9b9e-55b82cd58f86", Colors.GREEN, Categories.COMPO);

        Surprise SE112 = new Surprise("Leone", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE112.jpg?alt=media&token=08325377-94e0-4385-9ef7-601b4fd13adb", "SE112", cuccsav);
        Surprise SE113 = new Surprise("Scimmia", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE113.jpg?alt=media&token=1fbeba92-8f87-49f8-adb3-0105a04d8d9c", "SE113", cuccsav);
        Surprise SE114 = new Surprise("Coccodrillo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE114.jpg?alt=media&token=65d43d59-2cb9-4e4f-9348-2d2c3dd9f640", "SE114", cuccsav);
        Surprise SE115 = new Surprise("Elefante", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE115.jpg?alt=media&token=e1415b61-e084-48b1-9287-7faba4ca85dd", "SE115", cuccsav);

        insertSet(cuccsav);
        insertSurprise(SE112);
        insertSurprise(SE113);
        insertSurprise(SE114);
        insertSurprise(SE115);
        //endregion

        //region Maghi
        Set maghi = new Set("Maghi", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Maghi_2017.jpg?alt=media&token=f23491c2-93ed-486a-b2c2-6e8a6121ff9a", Colors.ORANGE, Categories.COMPO);

        Surprise SE123 = new Surprise("Mago Cappello Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE123.jpg?alt=media&token=3ebd21a6-0ef8-4b42-b104-1a6b540f806d", "SE123", maghi);
        Surprise SE129 = new Surprise("Mago Cappello Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE129.jpg?alt=media&token=52fc378c-dcb9-40d4-8ea4-b3fe6c715ef1", "SE129", maghi);
        Surprise SE129A = new Surprise("Mago Cappello Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE129A.jpg?alt=media&token=0f02326a-d0a9-4a28-82a4-acddad2f8cbb", "SE129A", maghi);
        Surprise SE130 = new Surprise("Mago Cappello Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE130.jpg?alt=media&token=ff36ad14-d403-4565-8a93-e0c082fd7114", "SE130", maghi);

        insertSet(maghi);
        insertSurprise(SE123);
        insertSurprise(SE129);
        insertSurprise(SE129A);
        insertSurprise(SE130);
        //endregion

        //region StreetPenGang
        Set strpen = new Set("Street pen gang", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_StreetPenGang_2017.jpg?alt=media&token=1c939a03-29ea-44b6-af97-8dfb6be034f4", Colors.ORANGE, Categories.COMPO);

        Surprise SE124 = new Surprise("Cappello Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE124.jpg?alt=media&token=725f38a2-dc35-4624-bc16-0e3e9d6642a3", "SE124", strpen);
        Surprise SE125 = new Surprise("Cappello Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE125.jpg?alt=media&token=9d77f466-513a-4849-a8ce-c2a05d664491", "SE125", strpen);
        Surprise SE126 = new Surprise("Cappello Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE126.jpg?alt=media&token=ffe41fc0-469d-4e35-8591-662b17560993", "SE126", strpen);
        Surprise SE127 = new Surprise("Cappello Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE127.jpg?alt=media&token=1996f8b3-32dd-4b03-aa9f-f49b954cfcfb", "SE127", strpen);

        insertSet(strpen);
        insertSurprise(SE124);
        insertSurprise(SE125);
        insertSurprise(SE126);
        insertSurprise(SE127);
        //endregion

        //region AnelliSplendenti
        Set ansplen = new Set("Anelli splendenti", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AnelliSplendenti_2017.jpg?alt=media&token=048ce84f-2343-475e-88e2-87e63fb1d1ec", Colors.PURPLE, Categories.COMPO);

        Surprise SE131 = new Surprise("Anello Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE131.jpg?alt=media&token=f32b5887-f859-40a3-ae47-f80f79b31d5b", "SE131", ansplen);
        Surprise SE132 = new Surprise("Anello Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE132.jpg?alt=media&token=17a4eea9-4d9f-48f3-82a5-1f08b88a47c2", "SE132", ansplen);
        Surprise SE133 = new Surprise("Anello Azzurro", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE133.jpg?alt=media&token=929b6091-d17b-4999-a49f-ec0c5e35746f", "SE133", ansplen);
        Surprise SE135 = new Surprise("Anello Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE135.jpg?alt=media&token=b08050ea-7906-4bbc-81c5-7087fb92254c", "SE135", ansplen);

        insertSet(ansplen);
        insertSurprise(SE131);
        insertSurprise(SE132);
        insertSurprise(SE133);
        insertSurprise(SE135);
        //endregion

        //region UccelliPittori
        Set uccpit = new Set("Uccelli pittori", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_UccelliPittori_2017.jpg?alt=media&token=07c30391-6277-4c26-b66e-b40289716940", Colors.ORANGE, Categories.COMPO);

        Surprise SE136 = new Surprise("Uccello Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE136.jpg?alt=media&token=585feea9-b5a9-4c6c-800e-6f8fea17ef5c", "SE136", uccpit);
        Surprise SE138 = new Surprise("Uccello Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE138.jpg?alt=media&token=b83a6ccf-be48-4f28-b85a-bb5abd18d4a6", "SE138", uccpit);
        Surprise SE139 = new Surprise("Uccello Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE139.jpg?alt=media&token=adfefd50-4c49-478b-b41a-de144c954d89", "SE139", uccpit);
        Surprise SE140 = new Surprise("Uccello Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE140.jpg?alt=media&token=8f14e9f7-22a3-4245-9c37-40330d01bae4", "SE140", uccpit);

        insertSet(uccpit);
        insertSurprise(SE136);
        insertSurprise(SE138);
        insertSurprise(SE139);
        insertSurprise(SE140);
        //endregion

        //region TimbriDellaSavana
        Set timsav = new Set("Timbri della Savana", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_TimbriDellaSavana_2017.jpg?alt=media&token=7ff7f103-0cb7-4421-95b4-4f1f5dd6f595", Colors.ORANGE, Categories.COMPO);

        Surprise SE142 = new Surprise("Elefante", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE142.jpg?alt=media&token=62be96cb-5371-4654-9651-0498b1afe2d1", "SE142", timsav);
        Surprise SE142A = new Surprise("Giraffa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE142A.jpg?alt=media&token=edc45394-8ad7-4ec0-9e5c-08c603ff0c55", "SE142A", timsav);
        Surprise SE142B = new Surprise("Leone", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE142B.jpg?alt=media&token=97b6312f-0b91-4782-97c0-7fabf82a7256", "SE142B", timsav);
        Surprise SE142C = new Surprise("Rinoceronte", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE142C.jpg?alt=media&token=98d5c327-f48d-405a-b9d5-491f3ce214f8", "SE142C", timsav);

        insertSet(timsav);
        insertSurprise(SE142);
        insertSurprise(SE142A);
        insertSurprise(SE142B);
        insertSurprise(SE142C);
        //endregion

        //region Safiras
        Set safiras = new Set("Safiras", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Safiras_2017.jpg?alt=media&token=182e11c5-17d2-4916-96b4-47dc29b6d1bf", Colors.PURPLE, Categories.COMPO);

        Surprise SE143 = new Surprise("Draghetto Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE143.jpg?alt=media&token=30566768-c504-4cc0-ac0a-8ddce6924622", "SE143", safiras);
        Surprise SE144 = new Surprise("Draghetto Azzurro", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE144.jpg?alt=media&token=ff2d7b87-5c63-4ae8-90c4-7f559f3d17ad", "SE144", safiras);
        Surprise SE146 = new Surprise("Draghetto Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE146.jpg?alt=media&token=5a072fc9-f710-4900-93b3-64c76cc58231", "SE146", safiras);
        Surprise SE147 = new Surprise("Draghetto Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE147.jpg?alt=media&token=e745ce89-61fd-4123-8402-ee48b26475a6", "SE147", safiras);

        insertSet(safiras);
        insertSurprise(SE143);
        insertSurprise(SE144);
        insertSurprise(SE146);
        insertSurprise(SE147);
        //endregion

        //region Oddbods
        Set oddbods = new Set("Oddbods", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_Oddbods_2017.jpg?alt=media&token=1c29b5b4-0f8f-4e2c-88dc-d5d17e7da302", Colors.BLUE, Categories.COMPO);

        Surprise SE148 = new Surprise("Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE148.jpg?alt=media&token=7ea02503-1fc5-4c08-8ff4-cc8eaaedc1b5", "SE148", oddbods);
        Surprise SE149 = new Surprise("Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE149.jpg?alt=media&token=80b9d5a1-4c48-4ec3-a9ad-ef857b98bab1", "SE149", oddbods);
        Surprise SE150 = new Surprise("Rosso", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE150.jpg?alt=media&token=827b6930-5589-4066-aa38-8cceed411267", "SE150", oddbods);
        Surprise SE151 = new Surprise("Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE151.jpg?alt=media&token=cc977603-1b46-4299-8393-c2a3056a1272", "SE151", oddbods);

        insertSet(oddbods);
        insertSurprise(SE148);
        insertSurprise(SE149);
        insertSurprise(SE150);
        insertSurprise(SE151);
        //endregion

        //region AnelliDellaFortuna
        Set anefort = new Set("Anelli della fortuna", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AnelliPortaFortuna_2017.jpg?alt=media&token=3c685c8a-ffdb-461e-96cb-642c2a86ccb6", Colors.PURPLE, Categories.COMPO);

        Surprise SE157 = new Surprise("Anello Verde Acqua", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE157.jpg?alt=media&token=bcee10f2-227c-4660-8347-bfef22ba888b", "SE157", anefort);
        Surprise SE157A = new Surprise("Anello Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE157A.jpg?alt=media&token=2d641b2d-62c2-41d6-8e56-9ac91b24aa9b", "SE157A", anefort);
        Surprise SE157B = new Surprise("Anello Viola", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE157B.jpg?alt=media&token=d1040cdb-48e6-4a12-a317-3a006e009c96", "SE157B", anefort);

        insertSet(anefort);
        insertSurprise(SE157);
        insertSurprise(SE157A);
        insertSurprise(SE157B);
        //endregion

        //region AnimaliDellaForesta
        Set animfor = new Set("Animali della foresta", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AnimaliDellaForesta_2017.jpg?alt=media&token=a7c449e6-d0c3-458a-9836-c533d92bfcd8", Colors.GREEN, Categories.COMPO);

        Surprise SE158 = new Surprise("Formichieri", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE158.jpg?alt=media&token=28341be8-885d-4944-be3e-1606a52445eb", "SE158", animfor);
        Surprise SE160 = new Surprise("Uccelli", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE160.jpg?alt=media&token=61ede8f8-27af-4d7f-b3d5-47c88e3d0df7", "SE160", animfor);
        Surprise SE161 = new Surprise("Scimmie", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE161.jpg?alt=media&token=d738cad2-fe61-4b11-a70a-5a2273a15afc", "SE161", animfor);
        Surprise SE162 = new Surprise("Camaleonti", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE162.jpg?alt=media&token=cf219d3b-6211-4fe7-a619-427e59af3bf1", "SE162", animfor);

        insertSet(animfor);
        insertSurprise(SE158);
        insertSurprise(SE160);
        insertSurprise(SE161);
        insertSurprise(SE162);
        //endregion

        //region AutoScattanti
        Set autscat = new Set("Auto scattanti", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AutoScattanti_2017.jpg?alt=media&token=a848a4fc-1794-4227-bdd2-6ec6cfc7f719", Colors.RED, Categories.COMPO);

        Surprise SE164 = new Surprise("Auto Bianca", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE164.jpg?alt=media&token=ef606c59-fd90-4c2d-8080-c707b7982ee5", "SE164", autscat);
        Surprise SE165 = new Surprise("Auto Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE165.jpg?alt=media&token=d90fbd08-478f-4100-84a5-151a555a88fb", "SE165", autscat);
        Surprise SE166 = new Surprise("Auto Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE166.jpg?alt=media&token=fd43ce66-9f48-4adf-8de4-cf958b6e72fc", "SE166", autscat);

        insertSet(autscat);
        insertSurprise(SE164);
        insertSurprise(SE165);
        insertSurprise(SE166);
        //endregion

        //region MolleAliene
        Set molalie = new Set("Molle aliene", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_MolleAliene_2017.jpg?alt=media&token=67fcea28-9ab1-4b20-a59a-d0944e0daf2c", Colors.BLUE, Categories.COMPO);

        Surprise SE179 = new Surprise("Alieno base Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE179.jpg?alt=media&token=614e95de-7660-4d0d-9d77-58617e7deafb", "SE179", molalie);
        Surprise SE180 = new Surprise("Alieno base Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE180.jpg?alt=media&token=18812460-bae7-4958-9a8b-12b9dd8abc65", "SE180", molalie);
        Surprise SE181 = new Surprise("Alieno base Nera", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE181.jpg?alt=media&token=abf8d8eb-158d-4601-8e37-f33908979a07", "SE181", molalie);
        Surprise SE182 = new Surprise("Alieno base Verde Acqua", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE182.jpg?alt=media&token=a7be4fda-0da3-49bd-ba2d-ad9b59692a7e", "SE182", molalie);

        insertSet(molalie);
        insertSurprise(SE179);
        insertSurprise(SE180);
        insertSurprise(SE181);
        insertSurprise(SE182);
        //endregion

        //region InsettiConLente
        Set inslen = new Set("Insetti con lente", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_InsettiConLente_2017.jpg?alt=media&token=3558c430-ee1e-48fa-a446-74956456e8ff", Colors.ORANGE, Categories.COMPO);

        Surprise SE201 = new Surprise("Lente Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE201.jpg?alt=media&token=5b93722b-2926-4e8c-a9d9-b4211bdec25d", "SE201", inslen);
        Surprise SE202 = new Surprise("Lente Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE202.jpg?alt=media&token=cee7414f-75c0-4be5-8a45-6ba503f06015", "SE202", inslen);
        Surprise SE203 = new Surprise("Lente Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE203.jpg?alt=media&token=3a780e1a-bf1b-4403-9545-4db052cdfd3f", "SE203", inslen);
        Surprise SE204 = new Surprise("Lente Rossa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE204.jpg?alt=media&token=974b7181-9a82-4452-bb42-7cb8328043ae", "SE204", inslen);

        insertSet(inslen);
        insertSurprise(SE201);
        insertSurprise(SE202);
        insertSurprise(SE203);
        insertSurprise(SE204);
        //endregion

        //region CuccioliConAli
        Set cuccali = new Set("Cuccioli con ali", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_CuccioliConAli_2017.jpg?alt=media&token=a6c4add1-743e-4cf9-b7a1-4ed9791f307b", Colors.GREEN, Categories.COMPO);

        Surprise SE205 = new Surprise("Cervo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE205.jpg?alt=media&token=1b4e9fb6-c70b-4841-b252-17765c8f8d77", "SE205", cuccali);
        Surprise SE206 = new Surprise("Cavallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE206.jpg?alt=media&token=9c2342b8-7ae1-4d1c-8b75-ea4ce982bc60", "SE206", cuccali);
        Surprise SE207 = new Surprise("Leone", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE207.jpg?alt=media&token=e915088e-b694-400e-b56d-4acf493cbef4", "SE207", cuccali);
        Surprise SE208 = new Surprise("Lupo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE208.jpg?alt=media&token=f6c8d374-ecee-4cb7-aa5b-6156623d31d7", "SE208", cuccali);

        insertSet(cuccali);
        insertSurprise(SE205);
        insertSurprise(SE206);
        insertSurprise(SE207);
        insertSurprise(SE208);
        //endregion

        //region FateDelBosco
        Set fatebosc = new Set("Fate del bosco", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_FateDelBosco_2017.jpg?alt=media&token=e3fb252a-b0c2-4d97-b5af-81c7b547caf3", Colors.PURPLE, Categories.COMPO);

        Surprise SE209 = new Surprise("Fata Azzurra", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE209.jpg?alt=media&token=9a374cc6-9b38-4743-bf15-ba2bb33f4b4c", "SE209", fatebosc);
        Surprise SE210 = new Surprise("Fata Rosa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE210.jpg?alt=media&token=dd726289-0eb9-4aab-a663-036d85a07116", "SE210", fatebosc);
        Surprise SE210A = new Surprise("Fata Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE210A.jpg?alt=media&token=93dfb5b1-e0b6-45f4-82d8-e8ca14819afb", "SE210A", fatebosc);

        insertSet(fatebosc);
        insertSurprise(SE209);
        insertSurprise(SE210);
        insertSurprise(SE210A);
        //endregion

        //region BamboleAllaModa
        Set bammod = new Set("Bambole alla moda", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_BamboleAllaModa_2017.jpg?alt=media&token=b6f1e50d-df6f-494a-80ed-d485c8af9dea", Colors.PURPLE, Categories.COMPO);

        Surprise SE212 = new Surprise("Capelli Neri", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE212.jpg?alt=media&token=29f1325b-9d41-4083-8a62-310d04e4f2ae", "SE212", bammod);
        Surprise SE212A = new Surprise("Capelli Castani", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE212A.jpg?alt=media&token=567d4cbb-3980-44a4-929d-6117bc1faf87", "SE212A", bammod);
        Surprise SE213 = new Surprise("Capelli Biondi", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE213.jpg?alt=media&token=1e3abd66-c9d2-42df-aa38-cc3265841f45", "SE213", bammod);
        Surprise SE213A = new Surprise("Capelli Rossi", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE213A.jpg?alt=media&token=e3f927c3-c9f9-4006-b6b1-6b31305815d4", "SE213A", bammod);

        insertSet(bammod);
        insertSurprise(SE212);
        insertSurprise(SE212A);
        insertSurprise(SE213);
        insertSurprise(SE213A);
        //endregion

        //region AutoACoppie
        Set autcop = new Set("Auto a coppie", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_AutoACoppie_2017.jpg?alt=media&token=52a79e55-d746-4aff-8abc-8df4f619a897", Colors.RED, Categories.COMPO);

        Surprise SE216 = new Surprise("Auto Blu/Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE216.jpg?alt=media&token=a34da429-0a77-4dc0-a94d-5d0cfe824cb0", "SE216", autcop);
        Surprise SE216A = new Surprise("Auto Blu/Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE216A.jpg?alt=media&token=ea783e50-0167-4925-956e-765e59a811fb", "SE216A", autcop);
        Surprise SE217 = new Surprise("Auto Viola/Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE217.jpg?alt=media&token=9ef5c8b4-b0ec-434a-9a95-db99a2ec3770", "SE217", autcop);
        Surprise SE217A = new Surprise("Auto Viola/Gialla", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE217A.jpg?alt=media&token=cd04a88c-e3e2-4a76-8c20-f2bd7bc6c06e", "SE217A", autcop);

        insertSet(autcop);
        insertSurprise(SE216);
        insertSurprise(SE216A);
        insertSurprise(SE217);
        insertSurprise(SE217A);
        //endregion

        //region PredatoriACaccia
        Set predcac = new Set("Predatori a caccia", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_PredatoriACaccia_2017.jpg?alt=media&token=762f6471-94e1-4e4c-a486-7a2596cda5f9", Colors.BLUE, Categories.COMPO);

        Surprise SE235 = new Surprise("Gatto", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE235.jpg?alt=media&token=aa351298-920d-4e1e-bfaa-50ef0f0988b7", "SE235", predcac);
        Surprise SE235A = new Surprise("Camaleonte", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE235A.jpg?alt=media&token=a9418cae-db61-4c94-a587-c2003f97cb6a", "SE235A", predcac);
        Surprise SE235B = new Surprise("Aquila", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE235B.jpg?alt=media&token=d8029f08-e6a2-4e6b-8a21-f3750b1371e8", "SE235B", predcac);
        Surprise SE235C = new Surprise("Volpe", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE235B.jpg?alt=media&token=d8029f08-e6a2-4e6b-8a21-f3750b1371e8", "SE235C", predcac);

        insertSet(predcac);
        insertSurprise(SE235);
        insertSurprise(SE235A);
        insertSurprise(SE235B);
        insertSurprise(SE235C);
        //endregion

        //region BraccialiDalMondo
        Set bracmon = new Set("Bracciali dal mondo ", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_BraccialiDalMondo_2017.jpg?alt=media&token=bf20cebb-ea55-4d1c-ace4-74fe053c7edb", Colors.PURPLE, Categories.COMPO);

        Surprise SE241 = new Surprise("Circolo Polare", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE241.jpg?alt=media&token=1e2c1c3f-5768-44a6-8167-5f7bb73c8ce8", "SE241", bracmon);
        Surprise SE241A = new Surprise("Africa", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE241A.jpg?alt=media&token=e82903b1-331d-4473-9172-15b9651f4426", "SE241A", bracmon);
        Surprise SE241B = new Surprise("Australia", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE241B.jpg?alt=media&token=cfec89b6-1c10-4f3f-ad0d-0124a9ec6220", "SE241B", bracmon);

        insertSet(bracmon);
        insertSurprise(SE241);
        insertSurprise(SE241A);
        insertSurprise(SE241B);
        //endregion

        //region MostriciattoliSalterini
        Set mossal = new Set("Mostriciattoli salterini", kinSorp2017, kinderSorpresa, ExtraLocales.EUROPE, "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FBPZ_MostriciattoliSalterini_2017.jpg?alt=media&token=53fa108e-2348-4b33-9baa-7b9170557b5b", Colors.BLUE, Categories.COMPO);

        Surprise SE522 = new Surprise("Mostriciattolo Verde", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE522.jpg?alt=media&token=b30510d0-5322-4408-91cd-d1ec841e59f8", "SE522", mossal);
        Surprise SE524 = new Surprise("Mostriciattolo Giallo", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE524.jpg?alt=media&token=c59d41e8-023d-4da5-b764-d2e094362668", "SE524", mossal);
        Surprise SE526 = new Surprise("Mostriciattolo Blu", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE526.jpg?alt=media&token=67a24b29-406a-4be0-a09a-3b8fd16020dc", "SE526", mossal);
        Surprise SE527 = new Surprise("Mostriciattolo Arancione", "https://firebasestorage.googleapis.com/v0/b/collectionhelper.appspot.com/o/Kinder_Sorpresa%2F2017%2FSE527.jpg?alt=media&token=de3b2381-e8c0-415d-bdb0-a4639c6079ab", "SE527", mossal);

        insertSet(mossal);
        insertSurprise(SE522);
        insertSurprise(SE524);
        insertSurprise(SE526);
        insertSurprise(SE527);
        //endregion
    }

    private void insertMissing(Surprise surp) {
        missings.child(user.getUsername()).child(surp.getId()).setValue(true);
    }

    private void insertSurprise(Surprise surp){
        surprises.child(surp.getId()).setValue(surp);
        sets.child(surp.getSet_id()).child("surprises").child(surp.getId()).setValue(true);
    }

    private void insertSet(Set set){
        years.child(set.getYear_id()).child("sets").child(set.getId()).setValue(true);
        sets.child(set.getId()).setValue(set);
    }

    private void insertYear(Year year){
        years.child(year.getId()).setValue(year);
        producers.child(year.getProducerId()).child("years").child(year.getId()).setValue(true);
    }

    private void insertProducer(Producer producer){
        producers.child(producer.getId()).setValue(producer);
    }
}
