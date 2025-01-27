-----JUnit4-------
``` java
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringJUnit4Test {
    // Test methods
}
```
``` java
@RunWith(Parameterized.class)
public class ParameterizedTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
        });
    }

    private int input;
    private int expected;

    public ParameterizedTest(int input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertEquals(expected, Fibonacci.compute(input));
    }
}
```
-----JUnit5-------
``` java
@ExtendWith(SpringExtension.class)
public class SpringJunit5Test {

@ParameterizedTest
@ValueSource(strings = {"Hello", "World"})
void testWithStringParameter(String argument) {
    assertNotNull(argument);
}
```
``` java
}
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class MyCombinedTest {

    @Mock
    private MyRepository myRepository;

    @InjectMocks
    @Autowired
    private MyService myService;

// Test methods
}
```