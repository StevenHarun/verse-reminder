package com.versereminder.app.data.local

import com.versereminder.app.domain.model.ReadingPlan
import com.versereminder.app.domain.model.ReadingPlanDay
import com.versereminder.app.domain.model.VerseCategory

object SeedReadingPlans {

    val plans: List<ReadingPlan> = listOf(
        // PLAN 1: DAMAI SEJAHTERA (7 HARI)
        ReadingPlan(
            id = "peace_7d",
            title = "7 Hari Menemukan Damai Sejahtera",
            subtitle = "Ketenangan hati di tengah badai kehidupan",
            description = "Perjalanan 7 hari untuk meredakan kepanikan, melepaskan kecemasan pikiran, dan mengalami damai sejahtera Allah yang melampaui segala akal.",
            iconEmoji = "🕊️",
            totalDays = 7,
            category = VerseCategory.PEACE,
            days = listOf(
                ReadingPlanDay(
                    dayNumber = 1,
                    title = "Damai yang Melampaui Segala Akal",
                    verseReference = "Filipi 4:6-7",
                    verseTextId = "Janganlah hendaknya kamu kuatir tentang apapun juga, tetapi nyatakanlah dalam segala hal keinginanmu kepada Allah dalam doa dan permohonan dengan ucapan syukur. Damai sejahtera Allah, yang melampaui segala akal, akan memelihara hati dan pikiranmu dalam Kristus Yesus.",
                    verseTextEn = "Do not be anxious about anything, but in every situation, by prayer and petition, with thanksgiving, present your requests to God. And the peace of God, which transcends all understanding, will guard your hearts and your minds in Christ Jesus.",
                    devotionalText = "Kekhawatiran sering kali datang tanpa diundang saat kita menghadapi ketidakpastian. Namun, firman Tuhan tidak menyuruh kita memendamnya sendirian. Serahkanlah setiap detil kekhawatiranmu ke tangan Tuhan lewat doa dan ucapan syukur. Damai Allah bukanlah hilangnya masalah, melainkan kehadiran-Nya yang menenangkan batin kita.",
                    prayerText = "Ya Tuhan, aku menyerahkan segala kekhawatiran dan beban pikiranku kepada-Mu hari ini. Penuhilah hatiku dengan damai sejahtera-Mu yang melampaui segala akalku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 2,
                    title = "Damai yang Ditinggalkan Kristus",
                    verseReference = "Yohanes 14:27",
                    verseTextId = "Damai sejahtera Kutinggalkan bagimu. Damai sejahtera-Ku Kuberikan kepadamu, dan apa yang Kuberikan tidak seperti yang diberikan oleh dunia kepadamu. Janganlah gelisah dan gentar hatimu.",
                    verseTextEn = "Peace I leave with you; my peace I give you. I do not give to you as the world gives. Do not let your hearts be troubled and do not be afraid.",
                    devotionalText = "Dunia menawarkan ketenangan yang bergantung pada saldo bank, pujian orang, atau keadaan luar. Namun Kristus memberikan damai batiniah yang kekal, yang tetap kokoh meski dunia di sekitar kita sedang berguncang.",
                    prayerText = "Tuhan Yesus, terima kasih atas anugerah damai-Mu. Tenangkanlah hatiku dari kegelisahan dan ajarlah aku berpegang teguh pada janji-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 3,
                    title = "Beristirahat di Bawah Naungan-Nya",
                    verseReference = "Mazmur 91:1-2",
                    verseTextId = "Orang yang duduk dalam lindungan Yang Mahatinggi dan bermalam dalam naungan Yang Mahakuasa akan berkata kepada TUHAN: Tempat perlindunganku dan kubu pertahananku, Allahku, yang kupercayai.",
                    verseTextEn = "Whoever dwells in the shelter of the Most High will rest in the shadow of the Almighty. I will say of the Lord, 'He is my refuge and my fortress, my God, in whom I trust.'",
                    devotionalText = "Tuhan bukanlah tempat pelarian darurat sementara, melainkan rumah kediaman abadi bagi jiwa kita. Datanglah mendekat kepada-Nya, karena di bawah naungan sayap-Nya tidak ada kuasa yang dapat mencelakakan jiwamu.",
                    prayerText = "Tuhan Yang Mahatinggi, jadikanlah hadirat-Mu tempat peristirahatanku hari ini. Lindungilah aku dan keluargaku dalam naungan kasih-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 4,
                    title = "Kelegaan Bagi Jiwa yang Letih",
                    verseReference = "Matius 11:28-29",
                    verseTextId = "Marilah kepada-Ku, semua yang letih lesu dan berbeban berat, Aku akan memberi kelegaan kepadamu. Pikullah kuk yang Kupasang dan belajarlah pada-Ku, karena Aku lemah lembut dan rendah hati dan jiwamu akan mendapat ketenangan.",
                    verseTextEn = "Come to me, all you who are weary and burdened, and I will give you rest. Take my yoke upon you and learn from me, for I am gentle and humble in heart, and you will find rest for your souls.",
                    devotionalText = "Seringkali kita merasa lelah bukan hanya secara fisik, melainkan secara mental dan spiritual. Yesus mengundang kita dengan tangan terbuka bukan untuk memberi beban tambahan, melainkan memberi kelegaan sejati.",
                    prayerText = "Tuhan Yesus, aku datang kepada-Mu membawa segala keletihanku. Berikanlah kelegaan dan kekuatan baru bagi jiwaku hari ini. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 5,
                    title = "Gembala yang Membimbing ke Air Tenang",
                    verseReference = "Mazmur 23:1-3",
                    verseTextId = "TUHAN adalah gembalaku, takkan kekurangan aku. Ia membaringkan aku di padang yang berumput hijau, Ia membimbing aku ke air yang tenang; Ia menyegarkan jiwaku.",
                    verseTextEn = "The Lord is my shepherd, I lack nothing. He makes me lie down in green pastures, he leads me beside quiet waters, he refreshes my soul.",
                    devotionalText = "Seekor domba hanya bisa berbaring tenang bila ia merasa aman dari pemangsa dan kenyang. Tuhan adalah Gembala yang mengenal setiap kebutuhanmu dan rindu menuntunmu ke tempat perhentian yang menyegarkan.",
                    prayerText = "Ya Tuhan Gembalaku yang baik, tuntunlah langkahku hari ini dan segarkan jiwaku dengan firman dan kasih-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 6,
                    title = "Rancangan Damai Sejahtera",
                    verseReference = "Yeremia 29:11",
                    verseTextId = "Sebab Aku ini mengetahui rancangan-rancangan apa yang ada pada-Ku mengenai kamu, demikianlah firman TUHAN, yaitu rancangan damai sejahtera dan bukan rancangan kecelakaan, untuk memberikan kepadamu hari depan yang penuh harapan.",
                    verseTextEn = "For I know the plans I have for you, declares the Lord, plans to prosper you and not to harm you, plans to give you hope and a future.",
                    devotionalText = "Mungkin masa depanmu saat ini terlihat buram atau menakutkan. Ingatlah bahwa Tuhan yang merancang jalan hidupmu adalah Tuhan yang setia. Rancangan-Nya adalah damai sejahtera dan kepastian harapan.",
                    prayerText = "Bapa di Surga, aku meletakkan masa depanku ke dalam tangan-Mu yang penuh kuasa. Aku percaya rancangan-Mu adalah yang terbaik bagiku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 7,
                    title = "Hati yang Berakar Teguh",
                    verseReference = "Yesaya 26:3",
                    verseTextId = "Yang hatinya teguh Kaukawali dengan damai sejahtera, sebab kepada-Mulah ia percaya.",
                    verseTextEn = "You will keep in perfect peace those whose minds are steadfast, because they trust in you.",
                    devotionalText = "Damai yang sempurna adalah milik mereka yang memilih untuk terus memusatkan pandangannya kepada Tuhan, bukan kepada besarnya badai. Kunci keteguhan batin adalah percaya penuh kepada-Nya.",
                    prayerText = "Tuhan, teguhkanlah hatiku untuk selalu memandang kepada-Mu dalam segala musim hidup. Penuhilah hidupku dengan damai yang sempurna. Amin."
                )
            )
        ),

        // PLAN 2: KEKUATAN & MENGATASI RASA TAKUT (7 HARI)
        ReadingPlan(
            id = "strength_7d",
            title = "7 Hari Mengatasi Rasa Takut & Khawatir",
            subtitle = "Menemukan keberanian melalui janji penyertaan Allah",
            description = "Bangun keteguhan iman dan hancurkan belenggu ketakutan dengan merenungkan janji perlindungan Tuhan setiap hari.",
            iconEmoji = "🛡️",
            totalDays = 7,
            category = VerseCategory.STRENGTH,
            days = listOf(
                ReadingPlanDay(
                    dayNumber = 1,
                    title = "Terang dan Benteng Hidupku",
                    verseReference = "Mazmur 27:1",
                    verseTextId = "TUHAN adalah terangku dan keselamatanku, kepada siapakah aku harus takut? TUHAN adalah benteng hidupku, terhadap siapakah aku harus gemetar?",
                    verseTextEn = "The Lord is my light and my salvation—whom shall I fear? The Lord is the stronghold of my life—of whom shall I be afraid?",
                    devotionalText = "Kegelapan dan ketakutan tidak berdaya di hadapan terang Allah. Ketika Tuhan menjadi kubu pertahanan hidup kita, tidak ada ancaman yang mampu merebut keselamatan jiwa kita.",
                    prayerText = "Tuhan, jadilah terang di setiap jalan gelapku. Enyahkanlah rasa takut dari hatiku dan kuatkan imanku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 2,
                    title = "Penyertaan yang Menguatkan",
                    verseReference = "Yesaya 41:10",
                    verseTextId = "Janganlah takut, sebab Aku menyertai engkau, janganlah bimbang, sebab Aku ini Allahmu; Aku akan meneguhkan, bahkan akan menolong engkau; Aku akan memegang engkau dengan tangan kanan-Ku yang membawa kemenangan.",
                    verseTextEn = "So do not fear, for I am with you; do not be dismayed, for I am your God. I will strengthen you and help you; I will uphold you with my righteous right hand.",
                    devotionalText = "Alasan utama mengapa kita tidak perlu takut bukanlah karena kita kuat, melainkan karena Tuhan menyertai kita. Tangan kanan-Nya yang perkasa sanggup memegang dan menopang kita saat kita hendak jatuh.",
                    prayerText = "Ya Allahku, terima kasih karena Engkau tidak pernah meninggalkanku. Peganglah tanganku dan beri aku kekuatan baru hari ini. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 3,
                    title = "Kuat dan Teguhkanlah Hatimu",
                    verseReference = "Yosua 1:9",
                    verseTextId = "Bukankah telah Kuperintahkan kepadamu: kuatkan dan teguhkanlah hatimu? Janganlah kecut dan tawar hati, sebab TUHAN, Allahmu, menyertai engkau, ke mana pun engkau pergi.",
                    verseTextEn = "Have I not commanded you? Be strong and courageous. Do not be afraid; do not be discouraged, for the Lord your God will be with you wherever you go.",
                    devotionalText = "Menghadapi tantangan baru atau perubahan hidup membutuhkan keberanian. Perintah Tuhan untuk berani selalu dibarengi dengan jaminan penyertaan-Nya ke mana pun kita melangkah.",
                    prayerText = "Tuhan, teguhkanlah hatiku untuk melangkah maju tanpa ragu. Pimpinlah jalanku ke mana pun aku pergi. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 4,
                    title = "Roh Kekuatan, Kasih, dan Ketertiban",
                    verseReference = "2 Timotius 1:7",
                    verseTextId = "Sebab Allah memberikan kepada kita bukan roh ketakutan, melainkan roh yang membangkitkan kekuatan, kasih dan ketertiban.",
                    verseTextEn = "For the Spirit God gave us does not make us timid, but gives us power, love and self-discipline.",
                    devotionalText = "Ketakutan bukanlah berasal dari Tuhan. Roh Kudus yang ada di dalam diri kita menganugerahkan kuasa untuk bertindak, kasih untuk melayani, dan kejernihan pikiran untuk menguasai diri.",
                    prayerText = "Bapa, penuhilah aku dengan Roh Kudus-Mu agar aku hidup dalam keberanian, kasih yang tulus, dan pikiran yang jernih. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 5,
                    title = "Menyerahkan Beban Kekhawatiran",
                    verseReference = "1 Petrus 5:7",
                    verseTextId = "Serahkanlah segala kekuatiranmu kepada-Nya, sebab Ia yang memelihara kamu.",
                    verseTextEn = "Cast all your anxiety on him because he cares for you.",
                    devotionalText = "Tuhan sangat mempedulikan setiap helai rambut di kepalamu dan setiap tetes air matamu. Melemparkan kekhawatiran kepada Tuhan berarti mengakui keterbatasan kita dan mempercayai pemeliharaan-Nya.",
                    prayerText = "Tuhan yang penuh kasih, aku melepaskan kecemasanku hari ini kepada-Mu. Aku tahu Engkau sangat mempedulikan hidupku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 6,
                    title = "Tempat Perlindungan di Waktu Kesesakan",
                    verseReference = "Mazmur 46:1-2",
                    verseTextId = "Allah itu bagi kita tempat perlindungan dan kekuatan, sebagai penolong dalam kesesakan sangat terbukti. Sebab itu kita tidak akan takut, sekalipun bumi berubah.",
                    verseTextEn = "God is our refuge and strength, an ever-present help in trouble. Therefore we will not fear, though the earth give way and the mountains fall into the heart of the sea.",
                    devotionalText = "Sekalipun bumi bergoncang atau situasi di sekelilingmu berubah drastis, Allah tetap tidak berubah. Kesetiaan dan pertolongan-Nya selalu terbukti tepat pada waktu-Nya.",
                    prayerText = "Tuhan, jadilah perlindunganku dan pertolonganku yang nyata dalam setiap pergumulanku hari ini. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 7,
                    title = "Lebih Dari Pemenang",
                    verseReference = "Roma 8:31, 37",
                    verseTextId = "Jika Allah di pihak kita, siapakah yang akan melawan kita? Tetapi dalam semuanya itu kita lebih dari pada orang-orang yang menang, oleh Dia yang telah mengasihi kita.",
                    verseTextEn = "If God is for us, who can be against us? No, in all these things we are more than conquerors through him who loved us.",
                    devotionalText = "Kemenangan kita bukanlah hasil dari kemampuan kita sendiri, melainkan karena kasih Kristus yang telah mengalahkan maut. Bersama Tuhan, kita tidak hanya bertahan, melainkan berkemenangan.",
                    prayerText = "Terima kasih Tuhan Yesus atas kemenangan yang telah Kau berikan bagiku. Aku berjalan hari ini dengan penuh keyakinan di dalam kasih-Mu. Amin."
                )
            )
        ),

        // PLAN 3: KASIH & PENGAMPUNAN (14 HARI)
        ReadingPlan(
            id = "love_14d",
            title = "14 Hari Berakar Dalam Kasih Kristus",
            subtitle = "Menyelami kasih sejati, pengampunan, dan kebaikan",
            description = "Renungan 14 hari untuk menyembuhkan hati yang terluka, belajar mengampuni sesama, dan memancarkan kasih Kristus dalam kehidupan sehari-hari.",
            iconEmoji = "❤️",
            totalDays = 14,
            category = VerseCategory.LOVE,
            days = listOf(
                ReadingPlanDay(
                    dayNumber = 1,
                    title = "Kasih yang Teramat Besar",
                    verseReference = "Yohanes 3:16",
                    verseTextId = "Karena begitu besar kasih Allah akan dunia ini, sehingga Ia telah mengaruniakan Anak-Nya yang tunggal, supaya setiap orang yang percaya kepada-Nya tidak binasa, melainkan beroleh hidup yang kekal.",
                    verseTextEn = "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life.",
                    devotionalText = "Dasar dari segala kasih di dunia bermula dari kasih pengorbanan Allah di kayu salib. Kita dikasihi tanpa syarat dan diberi kehidupan kekal.",
                    prayerText = "Bapa surgawi, terima kasih atas kasih-Mu yang begitu agung dan tanpa syarat bagi hidupku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 2,
                    title = "Ciri-Ciri Kasih yang Sejati",
                    verseReference = "1 Korintus 13:4-5",
                    verseTextId = "Kasih itu sabar; kasih itu murah hati; ia tidak cemburu. Ia tidak memegahkan diri dan tidak sombong. Ia tidak melakukan yang tidak sopan dan tidak mencari keuntungan diri sendiri. Ia tidak pemarah dan tidak menyimpan kesalahan orang lain.",
                    verseTextEn = "Love is patient, love is kind. It does not envy, it does not boast, it is not proud. It does not dishonor others, it is not self-seeking, it is not easily angered, it keeps no record of wrongs.",
                    devotionalText = "Kasih sejati bukanlah sekadar perasaan hangat, melainkan komitmen tindakan yang sabar, murah hati, dan memaafkan kesalahan sesama.",
                    prayerText = "Tuhan, mampukanlah aku menunjukkan kasih yang sabar dan murah hati kepada orang-orang di sekitarku hari ini. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 3,
                    title = "Kasih yang Menutupi Pelanggaran",
                    verseReference = "1 Petrus 4:8",
                    verseTextId = "Tetapi yang terutama: kasihilah sungguh-sungguh seorang akan yang lain, sebab kasih menutupi banyak sekali dosa.",
                    verseTextEn = "Above all, love each other deeply, because love covers over a multitude of sins.",
                    devotionalText = "Kasih memilih untuk tidak memperbesar kesalahan orang lain, melainkan menawarkan pengampunan dan rekonsiliasi yang memulihkan.",
                    prayerText = "Ya Tuhan, karuniakanlah kepadaku hati yang lapang untuk mengampuni dan menutupi kekurangan sesamaku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 4,
                    title = "Mengasihi dalam Perbuatan Nyata",
                    verseReference = "1 Yohanes 3:18",
                    verseTextId = "Anak-anakku, marilah kita mengasihi bukan dengan perkataan atau dengan lidah, tetapi dengan perbuatan dan dalam kebenaran.",
                    verseTextEn = "Dear children, let us not love with words or speech but with actions and in truth.",
                    devotionalText = "Kata-kata manis tidak cukup untuk membuktikan kasih. Kasih Kristus selalu terwujud dalam pertolongan nyata dan integritas hidup.",
                    prayerText = "Tuhan, pakailah tanganku untuk menolong sesama dan menyatakan kasih-Mu lewat perbuatan nyata hari ini. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 5,
                    title = "Kasih yang Tidak Pernah Gagal",
                    verseReference = "1 Korintus 13:7-8",
                    verseTextId = "Ia menutupi segala sesuatu, percaya segala sesuatu, mengharapkan segala sesuatu, sabar menanggung segala sesuatu. Kasih tidak berkesudahan.",
                    verseTextEn = "It always protects, always trusts, always hopes, always perseveres. Love never fails.",
                    devotionalText = "Di dunia di mana banyak hal datang dan pergi, kasih Allah adalah satu-satunya hal yang kekal dan tidak pernah mengecewakan.",
                    prayerText = "Tuhan, bimbinglah aku untuk senantiasa berharap dan sabar menanggung segala perkara dalam kasih-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 6,
                    title = "Hukum Terbesar: Kasih",
                    verseReference = "Matius 22:37-39",
                    verseTextId = "Kasihilah Tuhan, Allahmu, dengan segenap hatimu dan dengan segenap jiwamu dan dengan segenap akal budimu. Dan hukum yang kedua: Kasihilah sesamamu manusia seperti dirimu sendiri.",
                    verseTextEn = "Love the Lord your God with all your heart and with all your soul and with all your mind. And the second is like it: Love your neighbor as yourself.",
                    devotionalText = "Seluruh inti hukum taurat dan firman Tuhan terangkum dalam dua perintah ini: mencintai Tuhan sepenuhnya dan mencintai sesama seperti diri sendiri.",
                    prayerText = "Ya Allah, penuhilah hatiku dengan cinta kepada-Mu dan kepekaan untuk mengasihi sesamaku setiap hari. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 7,
                    title = "Pengorbanan Seorang Sahabat",
                    verseReference = "Yohanes 15:13",
                    verseTextId = "Tidak ada kasih yang lebih besar dari pada kasih seorang yang memberikan nyawanya untuk sahabat-sahabatnya.",
                    verseTextEn = "Greater love has no one than this: to lay down one's life for one's friends.",
                    devotionalText = "Kristus telah menyebut kita sahabat-Nya dan telah memberikan nyawa-Nya bagi kita. Panggilan hidup kita adalah meneladani pengorbanan-Nya.",
                    prayerText = "Tuhan Yesus, terima kasih telah menjadi Sahabat sejatiku. Ajar aku setia mengasihi orang lain seperti Engkau mengasihiku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 8,
                    title = "Mengasihi Mereka yang Melukai",
                    verseReference = "Lukas 6:27-28",
                    verseTextId = "Kasihilah musuhmu, berbuatlah baik kepada orang yang membenci kamu; mintalah berkat bagi orang yang mengutuk kamu; berdoalah bagi orang yang mencaci kamu.",
                    verseTextEn = "Love your enemies, do good to those who hate you, bless those who curse you, pray for those who mistreat you.",
                    devotionalText = "Inilah ujian tertinggi kasih: membalas kebencian dengan doa berkat. Kasih ini membebaskan kita dari kepahitan dan dendam.",
                    prayerText = "Bapa, sembuhkanlah luka di hatiku dan mampukan aku mendoakan berkat bagi mereka yang pernah menyakitiku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 9,
                    title = "Mengampuni Sama Seperti Kristus",
                    verseReference = "Kolose 3:13",
                    verseTextId = "Sabarlah kamu seorang terhadap yang lain, dan ampunilah seorang akan yang lain apabila yang seorang menaruh dendam terhadap yang lain, sama seperti Tuhan telah mengampuni kamu, kamu perbuat jugalah demikian.",
                    verseTextEn = "Bear with each other and forgive one another if any of you has a grievance against someone. Forgive as the Lord forgave you.",
                    devotionalText = "Dasar kita mengampuni orang lain bukanlah karena mereka layak diampuni, melainkan karena kita sendiri telah menerima pengampunan yang berlimpah dari Kristus.",
                    prayerText = "Tuhan, lepaskanlah aku dari belenggu dendam. Berikan aku kekuatan untuk mengampuni dengan tulus. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 10,
                    title = "Mengenal Allah Melalui Kasih",
                    verseReference = "1 Yohanes 4:7-8",
                    verseTextId = "Saudara-saudaraku yang kekasih, marilah kita saling mengasihi, sebab kasih itu berasal dari Allah; dan setiap orang yang mengasihi, lahir dari Allah dan mengenal Allah.",
                    verseTextEn = "Dear friends, let us love one another, for love comes from God. Everyone who loves has been born of God and knows God.",
                    devotionalText = "Tanda nyata bahwa seseorang mengenal Allah adalah kemampuan dan kerinduannya untuk memancarkan kasih kepada orang lain.",
                    prayerText = "Tuhan, jadikanlah hidupku cermin yang memancarkan kehadiran kasih-Mu bagi dunia. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 11,
                    title = "Kasih yang Tak Terpisahkan",
                    verseReference = "Roma 8:38-39",
                    verseTextId = "Sebab aku yakin, bahwa baik maut, maupun hidup... tidak akan dapat memisahkan kita dari kasih Allah, yang ada dalam Kristus Yesus, Tuhan kita.",
                    verseTextEn = "For I am convinced that neither death nor life... will be able to separate us from the love of God that is in Christ Jesus our Lord.",
                    devotionalText = "Tidak ada satu pun penderitaan, kegagalan, atau kesulitan hidup yang sanggup memutuskan ikatan kasih Allah yang merengkuh jiwamu.",
                    prayerText = "Tuhan, terima kasih atas kepastian bahwa kasih-Mu abadi dan selalu menaungiku selamanya. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 12,
                    title = "Saling Menghormati dan Mengutamakan",
                    verseReference = "Roma 12:10",
                    verseTextId = "Hendaklah kamu saling mengasihi sebagai saudara dan saling mendahului dalam memberi hormat.",
                    verseTextEn = "Be devoted to one another in love. Honor one another above yourselves.",
                    devotionalText = "Kasih persaudaraan melenyapkan persaingan egois dan menumbuhkan rasa saling menghargai satu dengan yang lain.",
                    prayerText = "Bapa, berikanlah aku kerendahan hati untuk menghargai sesama dan mengutamakan kebaikan bersama. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 13,
                    title = "Saling Memikul Beban",
                    verseReference = "Galatia 6:2",
                    verseTextId = "Bertolong-tolonganlah menanggung bebanmu! Demikianlah kamu memenuhi hukum Kristus.",
                    verseTextEn = "Carry each other's burdens, and in this way you will fulfill the law of Christ.",
                    devotionalText = "Ketika kita hadir mendengarkan, mendoakan, dan meringankan beban saudara kita, kita sedang memenuhi hukum kasih Kristus secara utuh.",
                    prayerText = "Tuhan, bukalah mataku untuk melihat mereka yang sedang berbeban berat dan pakailah aku menjadi saluran penghiburan-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 14,
                    title = "Pengikat yang Mempersatukan",
                    verseReference = "Kolose 3:14",
                    verseTextId = "Dan di atas semuanya itu: kenakanlah kasih, sebagai pengikat yang mempersatukan dan menyempurnakan.",
                    verseTextEn = "And over all these virtues put on love, which binds them all together in perfect unity.",
                    devotionalText = "Kasih adalah mahkota dari segala kebajikan rohani, pengikat yang menyatukan hati dalam keselarasan dan kesempurnaan.",
                    prayerText = "Tuhan Yesus, penuhilah hidupku setiap hari dengan kasih-Mu yang menyatukan dan menyempurnakan segalanya. Amin."
                )
            )
        ),

        // PLAN 4: HIKMAT & PETUNJUK HIDUP (7 HARI)
        ReadingPlan(
            id = "wisdom_7d",
            title = "7 Hari Hikmat & Petunjuk Hidup",
            subtitle = "Menuntun keputusan hidup dengan kebijaksanaan firman",
            description = "Renungan 7 hari menggali hikmat ilahi dari kitab Amsal dan Yakobus untuk menuntun pekerjaan, keluarga, dan masa depanmu.",
            iconEmoji = "💡",
            totalDays = 7,
            category = VerseCategory.WISDOM,
            days = listOf(
                ReadingPlanDay(
                    dayNumber = 1,
                    title = "Permulaan Segala Hikmat",
                    verseReference = "Amsal 9:10",
                    verseTextId = "Permulaan hikmat adalah takut akan TUHAN, dan mengenal Yang Mahakudus adalah keertian.",
                    verseTextEn = "The fear of the Lord is the beginning of wisdom, and knowledge of the Holy One is understanding.",
                    devotionalText = "Hikmat sejati bukan diukur dari gelar atau kepintaran dunia, melainkan dari rasa hormat dan ketaatan yang mendalam kepada Allah Sang Pencipta.",
                    prayerText = "Tuhan, ajarlah hatiku untuk selalu menghormati dan tunduk pada otoritas firman-Mu setiap saat. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 2,
                    title = "Meminta Hikmat dengan Iman",
                    verseReference = "Yakobus 1:5",
                    verseTextId = "Tetapi apabila di antara kamu ada yang kekurangan hikmat, hendaklah ia memintakannya kepada Allah, — yang memberikan kepada semua orang dengan murah hati dan dengan tidak membangkit-bangkit — maka hal itu akan diberikan kepadanya.",
                    verseTextEn = "If any of you lacks wisdom, you should ask God, who gives generously to all without finding fault, and it will be given to you.",
                    devotionalText = "Ketika kamu buntu atau bingung mengambil keputusan penting, datanglah meminta petunjuk kepada Allah. Ia memberi dengan murah hati tanpa mencela.",
                    prayerText = "Ya Allah, berikanlah aku hikmat-Mu untuk membuat keputusan yang tepat dan menyenangkan hati-Mu hari ini. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 3,
                    title = "Menjaga Hati dengan Waspada",
                    verseReference = "Amsal 4:23",
                    verseTextId = "Jagalah hatimu dengan segala kewaspadaan, karena dari situlah terpancar kehidupan.",
                    verseTextEn = "Above all else, guard your heart, for everything you do flows from it.",
                    devotionalText = "Hati adalah pusat dari pikiran, motif, dan emosi kita. Apa yang kita biarkan masuk ke dalam hati akan menentukan arah hidup dan tutur kata kita.",
                    prayerText = "Tuhan, jagalah hatiku dari kepahitan, kesombongan, dan tipu daya dunia. Bersihkanlah motivasi batinku. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 4,
                    title = "Mempercayakan Jalan Hidup",
                    verseReference = "Amsal 3:5-6",
                    verseTextId = "Percayalah kepada TUHAN dengan segenap hatimu, dan janganlah bersandar kepada pengertianmu sendiri. Akuilah Dia dalam segala lakumu, maka Ia akan meluruskan jalanmu.",
                    verseTextEn = "Trust in the Lord with all your heart and lean not on your own understanding; in all your ways submit to him, and he will make your paths straight.",
                    devotionalText = "Pengertian manusia sangat terbatas, namun pandangan Allah menembus masa depan. Mengakui Tuhan dalam setiap rencana adalah kunci jalan hidup yang lurus.",
                    prayerText = "Tuhan, aku menyerahkan setiap rencanaku kepada-Mu. Tuntunlah langkah kakiku di jalan kebenaran-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 5,
                    title = "Perkataan yang Bijaksana",
                    verseReference = "Amsal 15:1-2",
                    verseTextId = "Jawaban yang lemah lembut meredakan kegeraman, tetapi perkataan yang pedas membangkitkan marah. Lidah orang bijak mengeluarkan pengetahuan, tetapi mulut orang bebal memancarkan kebodohan.",
                    verseTextEn = "A gentle answer turns away wrath, but a harsh word stirs up anger. The tongue of the wise adorns knowledge, but the mouths of fools gush folly.",
                    devotionalText = "Lidah memiliki kuasa untuk membangun atau meremukkan sesama. Hikmat memampukan kita memilih kata-kata yang menenangkan ketimbang membakar amarah.",
                    prayerText = "Tuhan, taruhlah penjaga pada bibirku agar perkataanku selalu membawa damai, berkat, dan penghiburan bagi orang lain. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 6,
                    title = "Mempergunakan Waktu dengan Baik",
                    verseReference = "Efesus 5:15-16",
                    verseTextId = "Karena itu, perhatikanlah dengan saksama, bagaimana kamu hidup, janganlah seperti orang bebal, tetapi seperti orang arif, dan pergunakanlah waktu yang ada, karena hari-hari ini adalah jahat.",
                    verseTextEn = "Be very careful, then, how you live—not as unwise but as wise, making the most of every opportunity, because the days are evil.",
                    devotionalText = "Waktu adalah anugerah Tuhan yang tidak dapat diulang. Orang yang bijak memprioritaskan hal-hal kekal di atas kesibukan fana yang sia-sia.",
                    prayerText = "Tuhan, ajar aku menghitung hari-hariku dan mempergunakan waktuku dengan bijaksana untuk memuliakan nama-Mu. Amin."
                ),
                ReadingPlanDay(
                    dayNumber = 7,
                    title = "Hikmat Murni dari Atas",
                    verseReference = "Yakobus 3:17",
                    verseTextId = "Tetapi hikmat yang dari atas adalah pertama-tama murni, selanjutnya pendamai, peramah, penurut, penuh belas kasihan dan buah-buah yang baik, tidak memihak dan tidak munafik.",
                    verseTextEn = "But the wisdom that comes from heaven is first of all pure; then peace-loving, considerate, submissive, full of mercy and good fruit, impartial and sincere.",
                    devotionalText = "Hikmat ilahi selalu menghasilkan buah yang manis: kedamaian, kemurahan, dan ketulusan hati. Hidup dalam hikmat Allah adalah hidup yang memberkati banyak jiwa.",
                    prayerText = "Tuhan, penuhilah hidupku dengan hikmat surga-Mu yang murni dan berbuah lebat bagi kemuliaan-Mu. Amin."
                )
            )
        )
    )

    fun getPlanById(id: String): ReadingPlan? = plans.find { it.id == id }
}
