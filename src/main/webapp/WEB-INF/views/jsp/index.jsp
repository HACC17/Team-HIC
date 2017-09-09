<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
	<!--<link rel="stylesheet" href="assets/css/bootstrap.min.css" />-->
	<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
	<!--<link rel="stylesheet" href="assets/css/styles.css" />-->
	<link rel="stylesheet" href="assets/css/jquery.dataTables.min.css" />
	<link rel="stylesheet" href="assets/css/buttons.dataTables.min.css" />

    <link rel="stylesheet" href="assets/css/bootstrap-slider.min.css" />
    <link rel="stylesheet" href="assets/css/styles.css" />
    
	<script src="assets/js/jquery/jquery-3.2.1.min.js" type="text/javascript"></script>
	<script src="assets/js/jquery/jquery-1.12.4.min.js" type="text/javascript"></script>
	<script src="assets/js/accounting.min.js" type="text/javascript"></script>
	<script src="assets/js/download.js" type="text/javascript"></script>
	<script src="assets/js/jquery/jquery.maskedinput.min.js" type="text/javascript"></script>
	<script src="assets/js/highcharts/highcharts.js" type="text/javascript"></script>
	<script src="assets/js/highcharts/data.js" type="text/javascript"></script>
	<script src="assets/js/highcharts/drilldown.js" type="text/javascript"></script>
	<script src="assets/js/highcharts/exporting.js" type="text/javascript"></script>
	<script src="assets/js/canvg.js" type="text/javascript"></script>

    <script src="assets/js/jquery/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="assets/js/bootstrap-slider.min.js" type="text/javascript"></script>
    <link href="https://fonts.googleapis.com/css?family=Oxygen:300,400,700|Raleway:300,400,700" rel="stylesheet"> 
</head>
<body class="sidebar-fixed">
<div class="wrapper" id="wrapper">
    <nav class="navbar navbar-oha navbar-fixed-top">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">OHA Grants</a>
        </div>
    </nav>
    <div class="collapse navbar-collapse" id="navbar-collapse-1">
        <div class="left-sidebar" id="left-sidebar">
            <div class="sidebar-scroll">
                <div id="filters-panel" class="filters-panel">
                    <h3 class="sr-only">Filters</h3>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-priority"><i class="fa fa-area-chart"></i>Strategic Priority<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div  id="toggle-priority" class="collapse">
                            <fieldset>

                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Culture</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Economic Self-Sufficiency</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Education</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Governance</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Health</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Land &amp; Water</label>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-result"><i class="fa fa-line-chart"></i>Strategic Result<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div  id="toggle-result" class="collapse">
                            <fieldset>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Achieve Pae Aina Sustainability</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Build Stability in Housing</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Decrease Chronic Disease Rates</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Exceed Education Standards</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Improve Family Lifestyle</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Increase Family Income</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Participate in Culture</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Priority Level</label>
                                
                                <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Value History and Culture</label>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="form-check filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#grant-type"><i class="fa fa-info-circle"></i>Grant Type<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div id="grant-type" class="collapse">
                            <fieldset>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Ahahui
                                </label>                    
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    BOT Initiative
                                </label>                   
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Community Grant
                                </label>                   
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Sponsorship
                                </label>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#location"><i class="fa fa-map-marker"></i>Location<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div  id="location" class="collapse">
                            <fieldset>            
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Continent
                                </label>             
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Hawaii
                                </label>          
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    International
                                </label>                   
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Kauai
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Lanai
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Maui
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Molokai
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Multiple
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Oahu
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Papahanaumokuakea
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Polynesia
                                </label>
                                <label class="form-check-label">
                                    <input class="form-check-input" type="checkbox" value="">
                                    Statewide
                                </label>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-organization"><i class="fa fa-home"></i>Organization<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div id="toggle-organization" class="collapse">
                            <fieldset>
                                <select id="organization" class="filter form-control" onchange="updateTable();" data-key="organization">
                                    <option value="">All</option>
                                        <option value="After School All-Stars">After School All-Stars</option>

                                        <option value="Agricultural Leadership Foundation of Hawaii">Agricultural Leadership Foundation of Hawaii</option>
                                    
                                        <option value="Aha Hipuu">Aha Hipuu</option>
                                    
                                        <option value="Aha Kukui o Molokai">Aha Kukui o Molokai</option>
                                    
                                        <option value="Aha Punana Leo, Inc.">Aha Punana Leo, Inc.</option>
                                    
                                        <option value="Ahahui Kiwila Hawaii O Moikeha">Ahahui Kiwila Hawaii O Moikeha</option>
                                    
                                        <option value="Ahahui Olelo Hawaii">Ahahui Olelo Hawaii</option>
                                    
                                        <option value="Ahupuaa o Molokai">Ahupuaa o Molokai</option>
                                    
                                        <option value="Akamai Foundation">Akamai Foundation</option>
                                    
                                        <option value="Ala Kahakai Trail Association">Ala Kahakai Trail Association</option>
                                    
                                        <option value="Alaska Federation of Natives">Alaska Federation of Natives</option>
                                    
                                        <option value="Aloha Aina Foundation">Aloha Aina Foundation</option>
                                    
                                        <option value="Aloha First">Aloha First</option>
                                    
                                        <option value="Alternative Structures International">Alternative Structures International</option>
                                    
                                        <option value="Alu Like, Inc.">Alu Like, Inc.</option>
                                    
                                        <option value="American Cancer Society">American Cancer Society</option>
                                    
                                        <option value="American Diabetes Association">American Diabetes Association</option>
                                    
                                        <option value="American University">American University</option>
                                    
                                        <option value="APAICS">APAICS</option>
                                    
                                        <option value="Asian and Pacific Islander American Health Forum">Asian and Pacific Islander American Health Forum</option>
                                    
                                        <option value="Asian and Pacific Islander American Vote, Inc.">Asian and Pacific Islander American Vote, Inc.</option>
                                    
                                        <option value="Asian and Pacific Islander Association">Asian and Pacific Islander Association</option>
                                    
                                        <option value="Association of Hawaiian Civic Clubs">Association of Hawaiian Civic Clubs</option>
                                    
                                        <option value="Awaiaulu, Inc. for UH Library and Information Science">Awaiaulu, Inc. for UH Library and Information Science</option>
                                    
                                        <option value="Beamer Solomon Halau o Poohala">Beamer Solomon Halau o Poohala</option>
                                    
                                        <option value="Big Island Resource Conservtion and Development Council">Big Island Resource Conservtion and Development Council</option>
                                    
                                        <option value="Bishop Museum">Bishop Museum</option>
                                    
                                        <option value="Boys and Girls Club of Hawai'i">Boys and Girls Club of Hawai&#039;i</option>
                                    
                                        <option value="Boys and Girls Club of Maui">Boys and Girls Club of Maui</option>
                                    
                                        <option value="Catholic Charities">Catholic Charities</option>
                                    
                                        <option value="CFA Hawaii">CFA Hawaii</option>
                                    
                                        <option value="Chaminade University of Honolulu">Chaminade University of Honolulu</option>
                                    
                                        <option value="Conservation Council for Hawaii">Conservation Council for Hawaii</option>
                                    
                                        <option value="Consuelo Zobel Alger Foundation">Consuelo Zobel Alger Foundation</option>
                                    
                                        <option value="Council for Native Hawaiian Advancement (CNHA)">Council for Native Hawaiian Advancement (CNHA)</option>
                                    
                                        <option value="Damien and Marianne Foundation (The)">Damien and Marianne Foundation (The)</option>
                                    
                                        <option value="David Ige Inauguration Organization">David Ige Inauguration Organization</option>
                                    
                                        <option value="Department of Hawaiian Home Lands">Department of Hawaiian Home Lands</option>
                                    
                                        <option value="Department of Human Services/Maui Adult Protection and Community Services">Department of Human Services/Maui Adult Protection and Community Services</option>
                                    
                                        <option value="Department of Labor and Industrial Relations">Department of Labor and Industrial Relations</option>
                                    
                                        <option value="Department of Land and Natural Resources">Department of Land and Natural Resources</option>
                                    
                                        <option value="East Maui Taro Festival">East Maui Taro Festival</option>
                                    
                                        <option value="Edith Kanakaole Foundation">Edith Kanakaole Foundation</option>
                                    
                                        <option value="Educational Services Hawaii Foundation">Educational Services Hawaii Foundation</option>
                                    
                                        <option value="Effective Planning Innovative Communication, Inc. (DBA Epic Ohana)">Effective Planning Innovative Communication, Inc. (DBA Epic Ohana)</option>
                                    
                                        <option value="Family Promise of Hawaii">Family Promise of Hawaii</option>
                                    
                                        <option value="Friends of Chinatown">Friends of Chinatown</option>
                                    
                                        <option value="Friends of Hawaii Volcanoes National Park">Friends of Hawaii Volcanoes National Park</option>
                                    
                                        <option value="Friends of Iolani Palace (The)">Friends of Iolani Palace (The)</option>
                                    
                                        <option value="Friends of Kona Pacific Public Charter School">Friends of Kona Pacific Public Charter School</option>
                                    
                                        <option value="Friends of Molokai High and Middle Schools Foundation">Friends of Molokai High and Middle Schools Foundation</option>
                                    
                                        <option value="Friends of the Future">Friends of the Future</option>
                                    
                                        <option value="Garden Island Resource Conservation & Development, Inc.">Garden Island Resource Conservation &amp; Development, Inc.</option>
                                    
                                        <option value="Goodwill Industries of Hawaii">Goodwill Industries of Hawaii</option>
                                    
                                        <option value="Habilitat">Habilitat</option>
                                    
                                        <option value="Habitat for Humanity West Hawaii">Habitat for Humanity West Hawaii</option>
                                    
                                        <option value="Hale Mua Cultural Group, Inc">Hale Mua Cultural Group, Inc</option>
                                    
                                        <option value="Hale O Na Alii - Halau O Kalakaua">Hale O Na Alii - Halau O Kalakaua</option>
                                    
                                        <option value="Haleiwa Main Street dba North Shore Chamber of Commerce">Haleiwa Main Street dba North Shore Chamber of Commerce</option>
                                    
                                        <option value="Halelea Arts Foundation">Halelea Arts Foundation</option>
                                    
                                        <option value="Hana Cultural Center">Hana Cultural Center</option>
                                    
                                        <option value="Hawaii Academy of Recording Arts">Hawaii Academy of Recording Arts</option>
                                    
                                        <option value="Hawaii Alliance for Community-Based Economic Development (HACBED)">Hawaii Alliance for Community-Based Economic Development (HACBED)</option>
                                    
                                        <option value="Hawaii Bariatric Society">Hawaii Bariatric Society</option>
                                    
                                        <option value="Hawaii Book and Music Festival">Hawaii Book and Music Festival</option>
                                    
                                        <option value="Hawaii Community Foundation">Hawaii Community Foundation</option>
                                    
                                        <option value="Hawaii Conservation Alliance Foundation">Hawaii Conservation Alliance Foundation</option>
                                    
                                        <option value="Hawaii Construction Career Days">Hawaii Construction Career Days</option>
                                    
                                        <option value="Hawaii Convention Center">Hawaii Convention Center</option>
                                    
                                        <option value="Hawaii Council for the Humanities">Hawaii Council for the Humanities</option>
                                    
                                        <option value="Hawaii First Community Ventures">Hawaii First Community Ventures</option>
                                    
                                        <option value="Hawaii Habitat for Humanity">Hawaii Habitat for Humanity</option>
                                    
                                        <option value="Hawaii Maoli">Hawaii Maoli</option>
                                    
                                        <option value="Hawaii Nature Center">Hawaii Nature Center</option>
                                    
                                        <option value="Hawaii Ponoi Foundation">Hawaii Ponoi Foundation</option>
                                    
                                        <option value="Hawaii Psychological Association">Hawaii Psychological Association</option>
                                    
                                        <option value="Hawaii Public Charter School Network">Hawaii Public Charter School Network</option>
                                    
                                        <option value="Hawaii Wildlife Fund">Hawaii Wildlife Fund</option>
                                    
                                        <option value="Hawaiian Canoe Racing Association">Hawaiian Canoe Racing Association</option>
                                    
                                        <option value="Hawaiian Civic Club of Honolulu">Hawaiian Civic Club of Honolulu</option>
                                    
                                        <option value="Hawaiian Civic Club of Waimanalo">Hawaiian Civic Club of Waimanalo</option>
                                    
                                        <option value="Hawaiian Community Assets">Hawaiian Community Assets</option>
                                    
                                        <option value="Hawaiian Education and Reinstatement Foundation">Hawaiian Education and Reinstatement Foundation</option>
                                    
                                        <option value="Hawaiian Kamalii Inc.">Hawaiian Kamalii Inc.</option>
                                    
                                        <option value="Hawaiian Mission Houses Historic Site and Archives">Hawaiian Mission Houses Historic Site and Archives</option>
                                    
                                        <option value="Hawaiinuiakea School of Hawaiian Knowledge">Hawaiinuiakea School of Hawaiian Knowledge</option>
                                    
                                        <option value="Hawai?i Maoli">Hawai?i Maoli</option>
                                    
                                        <option value="Helping Hands">Helping Hands</option>
                                    
                                        <option value="HI Tech Youth Network">HI Tech Youth Network</option>
                                    
                                        <option value="Hiilei Aloha, LLC">Hiilei Aloha, LLC</option>
                                    
                                        <option value="Hiipaka">Hiipaka</option>
                                    
                                        <option value="Historic Hawaii Foundation">Historic Hawaii Foundation</option>
                                    
                                        <option value="Hoa Aina o Makaha">Hoa Aina o Makaha</option>
                                    
                                        <option value="Honpa Hongwanji Mission of Hawaii">Honpa Hongwanji Mission of Hawaii</option>
                                    
                                        <option value="Hoolehua Homestead Association">Hoolehua Homestead Association</option>
                                    
                                        <option value="Hoomau Ke Ola, Inc.">Hoomau Ke Ola, Inc.</option>
                                    
                                        <option value="Hui Aloha Kiholo">Hui Aloha Kiholo</option>
                                    
                                        <option value="Hui Malama I Na Kupuna">Hui Malama I Na Kupuna</option>
                                    
                                        <option value="Hui Malama Learning Center">Hui Malama Learning Center</option>
                                    
                                        <option value="Hui Malama Ola Na Oiwi">Hui Malama Ola Na Oiwi</option>
                                    
                                        <option value="Hui No Ke Ola Pono">Hui No Ke Ola Pono</option>
                                    
                                        <option value="Hui o Hee Nalu, Inc.">Hui o Hee Nalu, Inc.</option>
                                    
                                        <option value="Hui o Laka">Hui o Laka</option>
                                    
                                        <option value="Hui Pu Laka Hawaiian Civic Club">Hui Pu Laka Hawaiian Civic Club</option>
                                    
                                        <option value="Hula Preservation Society">Hula Preservation Society</option>
                                    
                                        <option value="I Ola Lahui">I Ola Lahui</option>
                                    
                                        <option value="Institute for Native Pacific Education and Culture">Institute for Native Pacific Education and Culture</option>
                                    
                                        <option value="Ka Aha Hui Naauao">Ka Aha Hui Naauao</option>
                                    
                                        <option value="Ka Honua Momona International">Ka Honua Momona International</option>
                                    
                                        <option value="Ka Huli a Haloa">Ka Huli a Haloa</option>
                                    
                                        <option value="Ka Meheu Ohu o Ka Honu">Ka Meheu Ohu o Ka Honu</option>
                                    
                                        <option value="Ka Molokai Makahiki, Inc.">Ka Molokai Makahiki, Inc.</option>
                                    
                                        <option value="Ka Ohana O Kalaupapa">Ka Ohana O Kalaupapa</option>
                                    
                                        <option value="Ka Waianu o Haloa">Ka Waianu o Haloa</option>
                                    
                                        <option value="Kaala Farm, Inc.">Kaala Farm, Inc.</option>
                                    
                                        <option value="KAHEA">KAHEA</option>
                                    
                                        <option value="Kai Loa, Inc.">Kai Loa, Inc.</option>
                                    
                                        <option value="Kailapa Community Association">Kailapa Community Association</option>
                                    
                                        <option value="Kakoo Central Maui Hawaiian Civic Club">Kakoo Central Maui Hawaiian Civic Club</option>
                                    
                                        <option value="Kakoo Oiwi">Kakoo Oiwi</option>
                                    
                                        <option value="Kalihi-Palama Culture and Arts Society, Inc.">Kalihi-Palama Culture and Arts Society, Inc.</option>
                                    
                                        <option value="Kamaaha Education Initiative">Kamaaha Education Initiative</option>
                                    
                                        <option value="Kamakakuokalani - The Center for Hawaiian Studies">Kamakakuokalani - The Center for Hawaiian Studies</option>
                                    
                                        <option value="Kamehameha Schools">Kamehameha Schools</option>
                                    
                                        <option value="Kanalu">Kanalu</option>
                                    
                                        <option value="Kanehunamoku Voyaging Academy">Kanehunamoku Voyaging Academy</option>
                                    
                                        <option value="Kanu Hawaii">Kanu Hawaii</option>
                                    
                                        <option value="Kanu O Ka Aina Learning Ohana">Kanu O Ka Aina Learning Ohana</option>
                                    
                                        <option value="Kaonohi Foundation">Kaonohi Foundation</option>
                                    
                                        <option value="Karuna Project (The)">Karuna Project (The)</option>
                                    
                                        <option value="Kauai Community College">Kauai Community College</option>
                                    
                                        <option value="Kauai Food Bank, Inc.">Kauai Food Bank, Inc.</option>
                                    
                                        <option value="Kauakoko Foundation">Kauakoko Foundation</option>
                                    
                                        <option value="Ke Hoola o Lima Lani">Ke Hoola o Lima Lani</option>
                                    
                                        <option value="Ke Kukui Foundation">Ke Kukui Foundation</option>
                                    
                                        <option value="Kealohilani Serrao">Kealohilani Serrao</option>
                                    
                                        <option value="Keiki O Ka Aina Family Learning Centers">Keiki O Ka Aina Family Learning Centers</option>
                                    
                                        <option value="KEY Project">KEY Project</option>
                                    
                                        <option value="KFVE-TV">KFVE-TV</option>
                                    
                                        <option value="King Kamehameha Celebration Commission">King Kamehameha Celebration Commission</option>
                                    
                                        <option value="Kipahulu Ohana">Kipahulu Ohana</option>
                                    
                                        <option value="Kohe Malamalama o Kanaloa - Protect Kahoolawe Fund">Kohe Malamalama o Kanaloa - Protect Kahoolawe Fund</option>
                                    
                                        <option value="Kokua Kalihi Valley Comprehensive Family Services">Kokua Kalihi Valley Comprehensive Family Services</option>
                                    
                                        <option value="Komike Makua Punana Leo o Honolulu">Komike Makua Punana Leo o Honolulu</option>
                                    
                                        <option value="Koolauloa Commmunity Health and Wellness Center">Koolauloa Commmunity Health and Wellness Center</option>
                                    
                                        <option value="Koolauloa Hawaiian Civic Club">Koolauloa Hawaiian Civic Club</option>
                                    
                                        <option value="Koolaupoko Hawaiian Civic Club">Koolaupoko Hawaiian Civic Club</option>
                                    
                                        <option value="Kuaaina Ulu Auamo">Kuaaina Ulu Auamo</option>
                                    
                                        <option value="Kualapuu Public Conversion Charter School">Kualapuu Public Conversion Charter School</option>
                                    
                                        <option value="Kualoa-Heeia Ecumenical Youth (KEY) Project">Kualoa-Heeia Ecumenical Youth (KEY) Project</option>
                                    
                                        <option value="Kula No Na Poe Hawaii">Kula No Na Poe Hawaii</option>
                                    
                                        <option value="Kumano I Ke Ala O Makaweli">Kumano I Ke Ala O Makaweli</option>
                                    
                                        <option value="Kure Atoll Conservancy">Kure Atoll Conservancy</option>
                                    
                                        <option value="Laiopua 2020">Laiopua 2020</option>
                                    
                                        <option value="Lanai Culture and Heritage Center">Lanai Culture and Heritage Center</option>
                                    
                                        <option value="Lanai High and Elementary School">Lanai High and Elementary School</option>
                                    
                                        <option value="Lau Kanaka no Hawaii">Lau Kanaka no Hawaii</option>
                                    
                                        <option value="Let's Roll Foundation">Let&#039;s Roll Foundation</option>
                                    
                                        <option value="Lieutenant Governor's Office">Lieutenant Governor&#039;s Office</option>
                                    
                                        <option value="Living Life Source Foundation">Living Life Source Foundation</option>
                                    
                                        <option value="Lunalilo Home">Lunalilo Home</option>
                                    
                                        <option value="Ma Ka Hana Ka Ike">Ma Ka Hana Ka Ike</option>
                                    
                                        <option value="Making Dreams Come True, Valley of Rainbows">Making Dreams Come True, Valley of Rainbows</option>
                                    
                                        <option value="Malie Foundation">Malie Foundation</option>
                                    
                                        <option value="Mana Maoli">Mana Maoli</option>
                                    
                                        <option value="Maui Family Support Services, Inc.">Maui Family Support Services, Inc.</option>
                                    
                                        <option value="Maui Historical Society">Maui Historical Society</option>
                                    
                                        <option value="Maui Native Hawaiian Chamber of Commerce">Maui Native Hawaiian Chamber of Commerce</option>
                                    
                                        <option value="Maui Nui Botanical Gardens Inc.">Maui Nui Botanical Gardens Inc.</option>
                                    
                                        <option value="Maunaloa Elementary School">Maunaloa Elementary School</option>
                                    
                                        <option value="Merrie Monarch Festival">Merrie Monarch Festival</option>
                                    
                                        <option value="Moana's Hula Halau">Moana&#039;s Hula Halau</option>
                                    
                                        <option value="Moanalua Gardens Foundation, Inc.">Moanalua Gardens Foundation, Inc.</option>
                                    
                                        <option value="Molokai Community Service Council">Molokai Community Service Council</option>
                                    
                                        <option value="Molokai General Hospital">Molokai General Hospital</option>
                                    
                                        <option value="Molokai Habitat for Humanity, Inc.">Molokai Habitat for Humanity, Inc.</option>
                                    
                                        <option value="Molokai Middle School">Molokai Middle School</option>
                                    
                                        <option value="Na Aikane o Maui, Inc.">Na Aikane o Maui, Inc.</option>
                                    
                                        <option value="Na Kalai Waa">Na Kalai Waa</option>
                                    
                                        <option value="Na Koa Opio">Na Koa Opio</option>
                                    
                                        <option value="Na Koa Opio for Hanakehau Learning Farm">Na Koa Opio for Hanakehau Learning Farm</option>
                                    
                                        <option value="Na Maka o Papahanaumokuakea">Na Maka o Papahanaumokuakea</option>
                                    
                                        <option value="Na Mamo o Muolea">Na Mamo o Muolea</option>
                                    
                                        <option value="Na Moku Aupuni o Koolau Hui">Na Moku Aupuni o Koolau Hui</option>
                                    
                                        <option value="Na Poe Kokua">Na Poe Kokua</option>
                                    
                                        <option value="Na Pua Noeau">Na Pua Noeau</option>
                                    
                                        <option value="Na Pualei o Likolehua">Na Pualei o Likolehua</option>
                                    
                                        <option value="Na Wahine o Ke Kai">Na Wahine o Ke Kai</option>
                                    
                                        <option value="Naalehu Theatre">Naalehu Theatre</option>
                                    
                                        <option value="Nanakuli Housing Corporation">Nanakuli Housing Corporation</option>
                                    
                                        <option value="National American Indian Housing Council">National American Indian Housing Council</option>
                                    
                                        <option value="National Congress of American Indians">National Congress of American Indians</option>
                                    
                                        <option value="National Indian Education Association">National Indian Education Association</option>
                                    
                                        <option value="National Marine Sanctuary Foundation, Inc.">National Marine Sanctuary Foundation, Inc.</option>
                                    
                                        <option value="National Museum of American Indians">National Museum of American Indians</option>
                                    
                                        <option value="National Tropical Botanical Garden">National Tropical Botanical Garden</option>
                                    
                                        <option value="Native Arts and Culture Foundation">Native Arts and Culture Foundation</option>
                                    
                                        <option value="Native Hawaiian Chamber of Commerce">Native Hawaiian Chamber of Commerce</option>
                                    
                                        <option value="Native Hawaiian Education Association (NHEA)">Native Hawaiian Education Association (NHEA)</option>
                                    
                                        <option value="Native Hawaiian Hospitality Association (NaHHA)">Native Hawaiian Hospitality Association (NaHHA)</option>
                                    
                                        <option value="Native Hawaiian Legal Corporation">Native Hawaiian Legal Corporation</option>
                                    
                                        <option value="Native Hawaiian Organizations Association">Native Hawaiian Organizations Association</option>
                                    
                                        <option value="Native Hawaiian Roll Commission">Native Hawaiian Roll Commission</option>
                                    
                                        <option value="Native Nations Education Foundation">Native Nations Education Foundation</option>
                                    
                                        <option value="Nature Conservancy (The)">Nature Conservancy (The)</option>
                                    
                                        <option value="North Hawaii Community Hospital">North Hawaii Community Hospital</option>
                                    
                                        <option value="North Kohala Community Resource Center">North Kohala Community Resource Center</option>
                                    
                                        <option value="North Shore Community Land Trust">North Shore Community Land Trust</option>
                                    
                                        <option value="Ohana Ministries, Inc.">Ohana Ministries, Inc.</option>
                                    
                                        <option value="Olelo Community Media">Olelo Community Media</option>
                                    
                                        <option value="PA'I Foundation">PA&#039;I Foundation</option>
                                    
                                        <option value="Paa Pono Milolii">Paa Pono Milolii</option>
                                    
                                        <option value="Pacific American Foundation">Pacific American Foundation</option>
                                    
                                        <option value="Pacific Islander Health Partnership">Pacific Islander Health Partnership</option>
                                    
                                        <option value="Pacific Justice and Reconciliation Center">Pacific Justice and Reconciliation Center</option>
                                    
                                        <option value="PACT">PACT</option>
                                    
                                        <option value="Paepae o Heeia">Paepae o Heeia</option>
                                    
                                        <option value="Papa Ola Lokahi">Papa Ola Lokahi</option>
                                    
                                        <option value="Papahana Kuaola">Papahana Kuaola</option>
                                    
                                        <option value="Papakolea Community Development Corporation">Papakolea Community Development Corporation</option>
                                    
                                        <option value="Papakolea Hawaiian Civic Club">Papakolea Hawaiian Civic Club</option>
                                    
                                        <option value="Papaku No Kamehaikana">Papaku No Kamehaikana</option>
                                    
                                        <option value="Parents and Children Together">Parents and Children Together</option>
                                    
                                        <option value="Paukukalo Hawaiian Homes Community Association">Paukukalo Hawaiian Homes Community Association</option>
                                    
                                        <option value="Peninsula Conflict Resolution Center">Peninsula Conflict Resolution Center</option>
                                    
                                        <option value="Piilani Hawaiian Civic Club of Colorado">Piilani Hawaiian Civic Club of Colorado</option>
                                    
                                        <option value="Pohai o Kamehameha">Pohai o Kamehameha</option>
                                    
                                        <option value="Polynesian Voyaging Society">Polynesian Voyaging Society</option>
                                    
                                        <option value="Prince Kuhio Hawaiian Civic Club">Prince Kuhio Hawaiian Civic Club</option>
                                    
                                        <option value="Project Vision Hawaii">Project Vision Hawaii</option>
                                    
                                        <option value="Puuhonua Society">Puuhonua Society</option>
                                    
                                        <option value="Queen's Medical Center (The)">Queen&#039;s Medical Center (The)</option>
                                    
                                        <option value="Research Corporation of the University of Hawaii">Research Corporation of the University of Hawaii</option>
                                    
                                        <option value="Salvation Army-Family Treatment Services">Salvation Army-Family Treatment Services</option>
                                    
                                        <option value="Smithsonian Institution, National Museum of the American Indian">Smithsonian Institution, National Museum of the American Indian</option>
                                    
                                        <option value="Smithsonian National Museum of the American Indians">Smithsonian National Museum of the American Indians</option>
                                    
                                        <option value="St. Augustine by the Sea Catholic Church">St. Augustine by the Sea Catholic Church</option>
                                    
                                        <option value="Supporting the Language of Kauai, Inc.">Supporting the Language of Kauai, Inc.</option>
                                    
                                        <option value="The Biographical Research Center">The Biographical Research Center</option>
                                    
                                        <option value="The Estria Foundation">The Estria Foundation</option>
                                    
                                        <option value="The Medical Foundation for the Study of the Environment">The Medical Foundation for the Study of the Environment</option>
                                    
                                        <option value="The Queen's Medical Center">The Queen&#039;s Medical Center</option>
                                    
                                        <option value="The Salvation Army">The Salvation Army</option>
                                    
                                        <option value="The Trust for Public Land">The Trust for Public Land</option>
                                    
                                        <option value="Tri-Isle Resource Conservation & Development Council, Inc">Tri-Isle Resource Conservation &amp; Development Council, Inc</option>
                                    
                                        <option value="Uhane Pohaku Na Moku O Hawaii, Inc.">Uhane Pohaku Na Moku O Hawaii, Inc.</option>
                                    
                                        <option value="United States Veterans Initiative">United States Veterans Initiative</option>
                                    
                                        <option value="Univeristy of Hawaii - Office of Research Services">Univeristy of Hawaii - Office of Research Services</option>
                                    
                                        <option value="University of Hawaii">University of Hawaii</option>
                                    
                                        <option value="University of Hawaii - Leeward Community College">University of Hawaii - Leeward Community College</option>
                                    
                                        <option value="University of Hawaii - Office of Research Services">University of Hawaii - Office of Research Services</option>
                                    
                                        <option value="University of Hawaii - Office of Research Services on behalf of John A. Burns School of Medicine">University of Hawaii - Office of Research Services on behalf of John A. Burns School of Medicine</option>
                                    
                                        <option value="University of Hawaii at Hilo">University of Hawaii at Hilo</option>
                                    
                                        <option value="University of Hawaii Foundation">University of Hawaii Foundation</option>
                                    
                                        <option value="University of Hawaii Foundation on behalf of Kamakakuokalani">University of Hawaii Foundation on behalf of Kamakakuokalani</option>
                                    
                                        <option value="University of Hawaii on behalf of Maui College">University of Hawaii on behalf of Maui College</option>
                                    
                                        <option value="University of Sydney (the)">University of Sydney (the)</option>
                                    
                                        <option value="Waianae Coast Comprehensive Health Center">Waianae Coast Comprehensive Health Center</option>
                                    
                                        <option value="Waianae Community Re-Development Corporation">Waianae Community Re-Development Corporation</option>
                                    
                                        <option value="Waianae District Comprehensive Health & Hospital Board, Inc.">Waianae District Comprehensive Health &amp; Hospital Board, Inc.</option>
                                    
                                        <option value="Waianae Hawaiian Civic Club">Waianae Hawaiian Civic Club</option>
                                    
                                        <option value="Waianae Kai Hawaiian Homestead Association">Waianae Kai Hawaiian Homestead Association</option>
                                    
                                        <option value="Waikiki Community Center">Waikiki Community Center</option>
                                    
                                        <option value="Waimanalo Health Center">Waimanalo Health Center</option>
                                    
                                        <option value="Waimea Hawaiian Homesteaders Association">Waimea Hawaiian Homesteaders Association</option>
                                    
                                        <option value="Waiola Church">Waiola Church</option>
                                    
                                        <option value="Washington Pacific Committee">Washington Pacific Committee</option>
                                    
                                        <option value="West Honolulu Rotary Club">West Honolulu Rotary Club</option>
                                    
                                        <option value="Women Helping Women">Women Helping Women</option>
                                    
                                        <option value="YMCA of Honolulu">YMCA of Honolulu</option>
                                    
                                        <option value="Young Women's Christian Association of Oahu">Young Women&#039;s Christian Association of Oahu</option>
                                    
                                </select>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#status"><i class="fa fa-check-circle"></i>Grant Status<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div id="status" class="collapse">
                            <fieldset>
                                
                                 <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Awarded</label>
                                
                                 <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="">Pending</label>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#year"><i class="fa fa-calendar"></i>Fiscal Year<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div id="year" class="collapse">
                            <fieldset>
                                <span id="ex18-label-2a" class="hidden">Example low value</span>
                                <span id="ex18-label-2b" class="hidden">Example high value</span>
                                <input id="ex18b" type="text"/>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#amount"><i class="fa fa-dollar"></i>Grant Amount<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div id="amount" class="collapse">
                            <fieldset>
                                <div class="form-group">
                                    <label for="min-amount">min</label>
                                    <div class="input-group">
                                        <div class="input-group-addon">$</div>
                                        <input type="number" min="0" max="10000000" value="100000" class="filter form-control" id="min-amount" data-key="amount-gte">
                                    </div>
                                </div>
                                 <div class="form-group">
                                    <label for="min-amount">max</label>
                                    <div class="input-group">
                                        <div class="input-group-addon">$</div>
                                        <input type="number" min="0" max="10000000" value="1000000" class="filter form-control" id="max-amount" data-key="amount-lte">
                                    </div>
                                </div>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <label class="fieldset-label" data-toggle="collapse" data-target="#people"><i class="fa fa-users"></i>People Served<i class="toggle-icon fa fa-angle-left"></i></label>
                        <div id="people" class="collapse">
                            <fieldset>
                                <label for="min-total">Total people</label>
                                <fieldset>
                                    <div class="form-group">
                                        <label for="max-total">min</label>
                                        <input type="number" min="0" max="10000000" value="0" class="filter form-control" id="min-total" data-key="total-gte">
                                    </div>
                                    <div class="form-group">
                                        <label for="max-total">max</label>
                                        <input type="number" min="0" max="10000000" value="1000" class="filter form-control" id="max-total" data-key="total-lte">
                                    </div>
                                </fieldset>
                                <label for="min-hawaiians">Number of Native Hawaiians</label>
                                <fieldset>
                                    <div class="form-group">
                                        <label for="max-total">min</label>
                                        <input type="number" min="0" max="10000000" value="0" class="filter form-control" id="min-hawaiians" data-key="hawaiians-gte">
                                    </div>
                                    <div class="form-group">
                                        <label for="max-hawaiians">max</label>
                                        <input type="number" min="0" max="10000000" value="1000" class="filter form-control" id="max-hawaiians" data-key="hawaiians-lte">
                                    </div>
                                </fieldset>
                                <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                            </fieldset>
                        </div>
                    </div>
                    <div class="filter-group">
                        <button id="clear" class="btn btn-primary">Clear Filters</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="main-wrapper" class="main-wrapper">
        <div class="panes content-wrapper">
    	<div class="tab-pane tables" id="all">
            <div class="table-responsive">
                <%@ include file="/WEB-INF/views/jspf/grants-table.jspf" %>
            </div>
        </div>
        <div class="tab-pane charts" id="charts">
            <div class="demo-chart">
                <img src="assets/images/chart.png"/>
            </div>
        </div>
        </div>
        <footer>
            <div class="content-wrapper">
                <span class="copyright">&copy; <script>document.write(new Date().getFullYear());</script> <a href="https://www.oha.org/">Office of Hawaiian Affairs</a></span>
            </div>
        </footer>
    </div>
</div><!-- .wrapper -->

<%@ include file="/WEB-INF/views/jspf/all-grants.jspf" %>

<script src="assets/js/popper.min.js" type="text/javascript"></script>
<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="assets/js/scripts.min.js" type="text/javascript"></script>
<script src="assets/js/jquery/jquery.dataTables.min.js" type="text/javascript"></script>

<script src="assets/js/dataTables.bootstrap.min.js" type="text/javascript"></script>

<script src="assets/js/dataTables.buttons.min.js" type="text/javascript"></script>
<script src="assets/js/buttons.html5.min.js" type="text/javascript"></script>
<script src="assets/js/buttons.flash.min.js" type="text/javascript"></script>
<script src="assets/js/buttons.print.min.js" type="text/javascript"></script>
<script src="assets/js/jszip.min.js" type="text/javascript"></script>
<script src="assets/js/pdfmake.min.js" type="text/javascript"></script>
<script src="assets/js/vfs_fonts.js" type="text/javascript"></script>
<script src="assets/js/chartjs/Chart.min.js" type="text/javascript"></script>
<script src="assets/js/chartjs/Chart.bundle.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        $('.sidebar-scroll').slimScroll({
            height: '100%'
        });
    });
    
    $("#ex18b").slider({
        min: 2013,
        max: 2016,
        value: [2013, 2016],
        labelledby: ['ex18-label-2a', 'ex18-label-2b']
    });

    $('.collapse').on('shown.bs.collapse', function () {
        $(this).prev().find(".toggle-icon").removeClass("fa-angle-left").addClass("fa-angle-down");
    });

    //The reverse of the above on hidden event:

    $('.collapse').on('hidden.bs.collapse', function () {
        $(this).prev().find(".toggle-icon").removeClass("fa-angle-down").addClass("fa-angle-left");
    });
    $('.select-all').click(function(event) {
      if(this.checked) {
          // Iterate each checkbox
          $(':checkbox').each(function() {
              this.checked = true;
          });
      }
      else {
        $(':checkbox').each(function() {
              this.checked = false;
          });
      }
    });
</script>
</body>
</html>
