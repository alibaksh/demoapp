package com.example.demoapp.dashboard

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.demoapp.components.LiveDataResult
import com.example.demoapp.services.network.NYService
import com.example.demoapp.services.network.ServiceFactory
import com.example.demoapp.services.network.responses.ArticleResponse
import com.example.demoapp.ui.dashboard.DashboardViewModel
import io.reactivex.Maybe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.SocketException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class DashboardViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var nyService: NYService

//    @Mock
//    lateinit var resonse: ArticleResponse

    lateinit var dashboardViewModel: DashboardViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.dashboardViewModel = DashboardViewModel()
        ServiceFactory.nyService = this.nyService
//        resonse = resonse.getMockInstance(getListOfItems)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun fetchArticlesSuccess() {
        // Mock API response
        `when`(runBlocking { nyService.getMostViewed("all-sections", "7.json") })
            .thenAnswer {
                return@thenAnswer Maybe.just(ArticleResponse.getMockInstance(getListOfItems))
            }

        val observer = mock(Observer::class.java) as Observer<LiveDataResult<ArticleResponse>>
        dashboardViewModel.articleList.observeForever(observer)
        dashboardViewModel.getArticles()

        Thread.sleep(1000)

        Assert.assertNotNull(this.dashboardViewModel.articleList.value)
        Assert.assertEquals(
            LiveDataResult.Status.SUCCESS,
            this.dashboardViewModel.articleList.value?.status
        )
    }

    @Test
    fun fetchArticlesError() {
        `when`(runBlocking { nyService.getMostViewed("all-sections", "7.json") })
            .thenAnswer {
                return@thenAnswer Maybe.error<NetworkErrorException>(SocketException("No network here"))
            }

        val observer = mock(Observer::class.java) as Observer<LiveDataResult<ArticleResponse>>
        dashboardViewModel.articleList.observeForever(observer)
        dashboardViewModel.getArticles()

        this.dashboardViewModel.getArticles()

        Thread.sleep(1000)

        Assert.assertNotNull(this.dashboardViewModel.articleList.value)
        Assert.assertEquals(
            LiveDataResult.Status.ERROR,
            this.dashboardViewModel.articleList.value?.status
        )
        assert(this.dashboardViewModel.articleList.value?.err is Throwable)
    }

    @Test
    fun setLoadingVisibilityOnSuccess() {
        // Mock API response
        `when`(runBlocking { nyService.getMostViewed("all-sections", "7.json") })
            .thenAnswer {
                return@thenAnswer Maybe.just(ArticleResponse.getMockInstance(getListOfItems))
            }

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.dashboardViewModel)

        spiedViewModel.getArticles()
        Thread.sleep(1000)
        verify(spiedViewModel, times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun setLoadingVisibilityOnError() {
        // Mock API response
        `when`(runBlocking { nyService.getMostViewed("all-sections", "7.json") })
            .thenAnswer {
                return@thenAnswer Maybe.just(ArticleResponse.getMockInstance(getListOfItems))
            }

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.dashboardViewModel)

        spiedViewModel.getArticles()
        Thread.sleep(1000)
        verify(spiedViewModel, times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun setLoadingVisibilityOnNoData() {
        `when`(runBlocking { nyService.getMostViewed("all-sections", "7.json") })
            .thenReturn(Maybe.create {
                it.onComplete()
            })


        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.dashboardViewModel)

        spiedViewModel.getArticles()
        Thread.sleep(1000)
        verify(spiedViewModel, times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    val getListOfItems = """
        {
  "status": "OK",
  "copyright": "Copyright (c) 2020 The New York Times Company.  All Rights Reserved.",
  "num_results": 20,
  "results": [
    {
      "uri": "nyt://article/117117b9-f253-53c9-a3a1-64fc795124a3",
      "url": "https://www.nytimes.com/2020/03/12/world/europe/12italy-coronavirus-health-care.html",
      "id": 100000007030376,
      "asset_id": 100000007030376,
      "source": "New York Times",
      "published_date": "2020-03-12",
      "updated": "2020-03-13 12:35:57",
      "section": "World",
      "subsection": "Europe",
      "nytdsection": "world",
      "adx_keywords": "Coronavirus (2019-nCoV);Emergency Medical Treatment;Hospitals;Quarantines;Epidemics;Politics and Government;Italy;Lombardy (Italy)",
      "column": null,
      "byline": "By Jason Horowitz",
      "type": "Article",
      "title": "Italy’s Health Care System Groans Under Coronavirus — a Warning to the World",
      "abstract": "In less than three weeks, the virus has overloaded hospitals in northern Italy, offering a glimpse of what countries face if they cannot slow the contagion.",
      "des_facet": [
        "Coronavirus (2019-nCoV)",
        "Emergency Medical Treatment",
        "Hospitals",
        "Quarantines",
        "Epidemics",
        "Politics and Government"
      ],
      "org_facet": [
        
      ],
      "per_facet": [
        
      ],
      "geo_facet": [
        "Italy",
        "Lombardy (Italy)"
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "A makeshift emergency unit  at the Brescia hospital, in northern Italy, on Thursday.",
          "copyright": "Luca Bruno/Associated Press",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/03/12/world/12italy-virus1/merlin_170410329_41442a1b-44e6-425a-91f5-11ba9e0faf04-thumbStandard.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/12/world/12italy-virus1/merlin_170410329_41442a1b-44e6-425a-91f5-11ba9e0faf04-mediumThreeByTwo210.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/12/world/12italy-virus1/merlin_170410329_41442a1b-44e6-425a-91f5-11ba9e0faf04-mediumThreeByTwo440.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    },
    {
      "uri": "nyt://article/1e6764bd-62b3-53e6-9cd2-a56850fb15f2",
      "url": "https://www.nytimes.com/2020/03/06/world/middleeast/saudi-royal-arrest.html",
      "id": 100000007021065,
      "asset_id": 100000007021065,
      "source": "New York Times",
      "published_date": "2020-03-06",
      "updated": "2020-03-09 19:02:48",
      "section": "World",
      "subsection": "Middle East",
      "nytdsection": "world",
      "adx_keywords": "Political Prisoners;Politics and Government;Royal Families;Mohammed bin Salman (1985- );Saudi Arabia",
      "column": null,
      "byline": "By David D. Kirkpatrick and Ben Hubbard",
      "type": "Article",
      "title": "Saudi Prince Detains Senior Members of Royal Family",
      "abstract": "Crown Prince Mohammed bin Salman has detained a brother of the king, a former crown prince and a cousin, royals who may have been seen as threats to his rule.",
      "des_facet": [
        "Political Prisoners",
        "Politics and Government",
        "Royal Families"
      ],
      "org_facet": [
        
      ],
      "per_facet": [
        "Mohammed bin Salman (1985- )"
      ],
      "geo_facet": [
        "Saudi Arabia"
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "Crown Prince Mohammed bin Salman of Saudi Arabia, left, with Mohammed bin Nayef, the former crown prince.",
          "copyright": "Fayez Nureldine/Agence France-Presse — Getty Images",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/03/06/world/06saudi/06saudi-thumbStandard.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/06/world/06saudi/06saudi-mediumThreeByTwo210.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/06/world/06saudi/06saudi-mediumThreeByTwo440.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    },
    {
      "uri": "nyt://article/d778b20f-0858-5a9c-9381-d8b7833f4629",
      "url": "https://www.nytimes.com/2020/03/07/us/politics/erik-prince-project-veritas.html",
      "id": 100000007014650,
      "asset_id": 100000007014650,
      "source": "New York Times",
      "published_date": "2020-03-07",
      "updated": "2020-03-09 13:35:39",
      "section": "U.S.",
      "subsection": "Politics",
      "nytdsection": "u.s.",
      "adx_keywords": "Espionage and Intelligence Services;United States Politics and Government;Prince, Erik D;O'Keefe, James E III;Trump, Donald J;Seddon, Richard;Jorge, Marisa;Project Veritas;Defense Department",
      "column": null,
      "byline": "By Mark Mazzetti and Adam Goldman",
      "type": "Article",
      "title": "Erik Prince Recruits Ex-Spies to Help Infiltrate Liberal Groups",
      "abstract": "Mr. Prince, a contractor close to the Trump administration, contacted veteran spies for operations by Project Veritas, the conservative group known for conducting stings on news organizations and other groups.",
      "des_facet": [
        "Espionage and Intelligence Services",
        "United States Politics and Government"
      ],
      "org_facet": [
        "Project Veritas",
        "Defense Department"
      ],
      "per_facet": [
        "Prince, Erik D",
        "O'Keefe, James E III",
        "Trump, Donald J",
        "Seddon, Richard",
        "Jorge, Marisa"
      ],
      "geo_facet": [
        
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "Erik Prince, the former head of Blackwater Worldwide and the brother of Education Secretary Betsy DeVos, has at times served as an informal adviser to Trump administration officials.",
          "copyright": "Jeenah Moon/Reuters",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/03/08/us/politics/08dc-prince-p1/08dc-prince-p1-thumbStandard-v2.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/08/us/politics/08dc-prince-p1/08dc-prince-p1-mediumThreeByTwo210-v4.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/08/us/politics/08dc-prince-p1/08dc-prince-p1-mediumThreeByTwo440-v4.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    },
    {
      "uri": "nyt://article/70439aa8-c60f-5fae-9425-718217875dc2",
      "url": "https://www.nytimes.com/2020/01/23/smarter-living/adults-guide-to-social-skills.html",
      "id": 100000006681764,
      "asset_id": 100000006681764,
      "source": "New York Times",
      "published_date": "2020-01-23",
      "updated": "2020-01-29 10:24:37",
      "section": "Smarter Living",
      "subsection": "",
      "nytdsection": "smarter living",
      "adx_keywords": "Emotions;Psychology and Psychologists;Empathy;Dating and Relationships;Mental Health and Disorders",
      "column": null,
      "byline": "By Eric Ravenscraft",
      "type": "Article",
      "title": "An Adult’s Guide to Social Skills, for Those Who Were Never Taught",
      "abstract": "It’s a shame so few of us are taught the basics of how to interact constructively with each other. If you never were, we’re here to help.",
      "des_facet": [
        "Emotions",
        "Psychology and Psychologists",
        "Empathy",
        "Dating and Relationships",
        "Mental Health and Disorders"
      ],
      "org_facet": [
        
      ],
      "per_facet": [
        
      ],
      "geo_facet": [
        
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "",
          "copyright": "Evan Cohen",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/01/27/smarter-living/26sl_socialbasics/00sl_socialbasics-thumbStandard.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/01/27/smarter-living/26sl_socialbasics/00sl_socialbasics-mediumThreeByTwo210.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/01/27/smarter-living/26sl_socialbasics/00sl_socialbasics-mediumThreeByTwo440.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    },
    {
      "uri": "nyt://article/128bb2b5-4c50-5d38-9f82-58637f76b4a2",
      "url": "https://www.nytimes.com/2020/03/11/world/europe/coronavirus-merkel-germany.html",
      "id": 100000007028081,
      "asset_id": 100000007028081,
      "source": "New York Times",
      "published_date": "2020-03-11",
      "updated": "2020-03-13 12:35:47",
      "section": "World",
      "subsection": "Europe",
      "nytdsection": "world",
      "adx_keywords": "Coronavirus (2019-nCoV);Politics and Government;Merkel, Angela;Trump, Donald J;Germany",
      "column": null,
      "byline": "By Katrin Bennhold and Melissa Eddy",
      "type": "Article",
      "title": "Merkel Gives Germans a Hard Truth About the Coronavirus",
      "abstract": "The famously no-nonsense chancellor, keeping to form, braced Germany for an epidemic that may reach extraordinary scale.",
      "des_facet": [
        "Coronavirus (2019-nCoV)",
        "Politics and Government"
      ],
      "org_facet": [
        
      ],
      "per_facet": [
        "Merkel, Angela",
        "Trump, Donald J"
      ],
      "geo_facet": [
        "Germany"
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "“We have to understand that many people will be infected,” Chancellor Angela Merkel said Wednesday.",
          "copyright": "Omer Messinger/EPA, via Shutterstock",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/03/11/world/11Merkel1/11Merkel1-thumbStandard.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/11/world/11Merkel1/11Merkel1-mediumThreeByTwo210.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/11/world/11Merkel1/11Merkel1-mediumThreeByTwo440.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    },
    {
      "uri": "nyt://article/7c9dacb3-3f6b-55a3-acaa-6ea80f99b99c",
      "url": "https://www.nytimes.com/2020/03/07/nyregion/coronavirus-new-york-queens.html",
      "id": 100000007020807,
      "asset_id": 100000007020807,
      "source": "New York Times",
      "published_date": "2020-03-07",
      "updated": "2020-03-10 10:05:57",
      "section": "New York",
      "subsection": "",
      "nytdsection": "new york",
      "adx_keywords": "Coronavirus (2019-nCoV);Tests (Medical);Cuomo, Andrew M;New York State;New York City",
      "column": null,
      "byline": "By Jesse McKinley and Edgar Sandoval",
      "type": "Article",
      "title": "Coronavirus in N.Y.: Cuomo Declares State of Emergency",
      "abstract": "The announcement came as the number of patients in New York rose to 89, including a Queens driver who worked for Uber.",
      "des_facet": [
        "Coronavirus (2019-nCoV)",
        "Tests (Medical)"
      ],
      "org_facet": [
        
      ],
      "per_facet": [
        "Cuomo, Andrew M"
      ],
      "geo_facet": [
        "New York State",
        "New York City"
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "Gov. Andrew M. Cuomo, center, delivered an update on the coronavirus in New York at the state Capitol on Saturday.",
          "copyright": "Cindy Schultz for The New York Times",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/03/07/nyregion/07nyvirus-cuomo-1/07nyvirus-cuomo-thumbStandard.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/07/nyregion/07nyvirus-cuomo-1/07nyvirus-cuomo-mediumThreeByTwo210.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/07/nyregion/07nyvirus-cuomo-1/07nyvirus-cuomo-mediumThreeByTwo440.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    },
    {
      "uri": "nyt://article/fe377d59-62b8-53c2-9b22-e1de0a8a6ab2",
      "url": "https://www.nytimes.com/article/coronavirus-body-symptoms.html",
      "id": 100000007026228,
      "asset_id": 100000007026228,
      "source": "New York Times",
      "published_date": "2020-03-11",
      "updated": "2020-03-13 06:09:25",
      "section": "Health",
      "subsection": "",
      "nytdsection": "health",
      "adx_keywords": "your-feed-science;Coronavirus (2019-nCoV);Lungs;Respiratory System;Immune System;SARS (Severe Acute Respiratory Syndrome);Oxygen;Research;your-feed-healthcare",
      "column": null,
      "byline": "By Pam Belluck",
      "type": "Article",
      "title": "What Does the Coronavirus Do to the Body?",
      "abstract": "Here’s what scientists have learned about how the new virus infects and attacks cells and how it can affect organs beyond the lungs.",
      "des_facet": [
        "your-feed-science",
        "Coronavirus (2019-nCoV)",
        "Lungs",
        "Respiratory System",
        "Immune System",
        "SARS (Severe Acute Respiratory Syndrome)",
        "Oxygen",
        "Research",
        "your-feed-healthcare"
      ],
      "org_facet": [
        
      ],
      "per_facet": [
        
      ],
      "geo_facet": [
        
      ],
      "media": [
        {
          "type": "image",
          "subtype": "photo",
          "caption": "Doctors examined a patient’s lung scans at a hospital in Hubei province last month.",
          "copyright": "Agence France-Presse — Getty Images",
          "approved_for_syndication": 1,
          "media-metadata": [
            {
              "url": "https://static01.nyt.com/images/2020/03/11/science/11VIRUS-BODY-sub/11VIRUS-BODY-sub-thumbStandard.jpg",
              "format": "Standard Thumbnail",
              "height": 75,
              "width": 75
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/11/science/11VIRUS-BODY-sub/11VIRUS-BODY-sub-mediumThreeByTwo210.jpg",
              "format": "mediumThreeByTwo210",
              "height": 140,
              "width": 210
            },
            {
              "url": "https://static01.nyt.com/images/2020/03/11/science/11VIRUS-BODY-sub/11VIRUS-BODY-sub-mediumThreeByTwo440.jpg",
              "format": "mediumThreeByTwo440",
              "height": 293,
              "width": 440
            }
          ]
        }
      ],
      "eta_id": 0
    }
  ]
}
        """
}
