package cmc.mellyserver.mellycore.scrap.repository;

import cmc.mellyserver.mellycore.common.RepositoryTest;


public class ScrapRepositoryTest extends RepositoryTest {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PlaceRepository placeRepository;
//
//    @Autowired
//    private PlaceScrapRepository placeScrapRepository;
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    User user;
//
//    Place place;
//
//    @BeforeEach
//    void setUp() {
//
//        user = userRepository.save(fixture.UserFixtures.테스트_유저_1());
//        place = placeRepository.save(cmc.mellyserver.mellycore.common.fixture.PlaceFixtures.테스트_장소_1());
//
//        placeScrapRepository.save(PlaceScrap.createScrap(user, place, ScrapType.FRIEND));
//
//        testEntityManager.flush();
//        testEntityManager.clear();
//    }
//
//
//    @DisplayName("스크랩을 한 유저의 ID와 장소 ID로 해당 스크랩을 찾을 수 있다")
//    @Test
//    void 스크랩을한_유저의_ID와_장소ID로_스크랩을_찾을수_있다() {
//
//        // when
//        Optional<PlaceScrap> result = placeScrapRepository.findByUserIdAndPlaceId(user.getId(), place.getId());
//
//        // then
//        Assertions.assertThat(result.get().getUser().getNickname()).isEqualTo(user.getNickname());
//        Assertions.assertThat(result.get().getPlace().getPlaceName()).isEqualTo(place.getPlaceName());
//    }
}
