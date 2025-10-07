# ExpenseTracker Testing Guide

This document provides comprehensive information about the testing setup and strategy for the ExpenseTracker Android application.

## Testing Architecture

The app follows a comprehensive testing strategy with three levels of testing:

### 1. Unit Tests (`src/test/`)
- **Location**: `app/src/test/java/`
- **Purpose**: Test individual components in isolation
- **Framework**: JUnit 4, Kotlin Test
- **Coverage**: Data classes, utility functions

### 2. Instrumentation Tests (`src/androidTest/`)
- **Location**: `app/src/androidTest/java/`
- **Purpose**: Test Android-specific components and UI
- **Framework**: AndroidX Test, Compose Test, MockK
- **Coverage**: UI components, database integration, ViewModels

### 3. End-to-End Tests
- **Location**: `app/src/androidTest/java/`
- **Purpose**: Test complete user flows
- **Framework**: Compose Test with real app context

## Test Categories

### UI Component Tests
- **DashboardScreenTest**: Tests the main dashboard UI
- **AddCategoryDialogTest**: Tests category selection dialog
- **AddExpenseFormDialogTest**: Tests expense form dialog
- **BottomNavigationTest**: Tests navigation component

### Data Layer Tests
- **ExpenseDatabaseTest**: Tests Room database operations
- **ExpenseTest**: Tests data class functionality

### Business Logic Tests
- **ExpenseViewModelTest**: Tests ViewModel behavior and data flow

### Integration Tests
- **ExpenseTrackerE2ETest**: Tests complete user workflows

## Running Tests

### Run All Tests
```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Run all instrumentation tests (requires connected device/emulator)
./gradlew connectedDebugAndroidTest

# Run specific test class
./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.expensetracker.ui.screens.DashboardScreenTest
```

### Run Test Suite
```bash
# Run the complete test suite
./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.expensetracker.ExpenseTrackerTestSuite
```

### Build Test APKs
```bash
# Build instrumentation test APK
./gradlew assembleDebugAndroidTest

# Build main app APK
./gradlew assembleDebug
```

## Test Dependencies

### Core Testing Libraries
- **JUnit 4**: Unit testing framework
- **AndroidX Test**: Android testing utilities
- **Compose Test**: UI testing for Jetpack Compose
- **MockK**: Mocking framework for Kotlin
- **Coroutines Test**: Testing coroutines and flows
- **Room Testing**: Database testing utilities

### Configuration
```kotlin
// Testing dependencies in build.gradle.kts
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
androidTestImplementation("io.mockk:mockk-android:1.13.8")
androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
androidTestImplementation("androidx.room:room-testing:2.6.1")
```

## Test Coverage

### UI Components ✅
- Dashboard screen rendering and interactions
- Dialog components (category selection, expense form)
- Navigation between tabs
- Form validation and error handling
- User input handling

### Data Layer ✅
- Database CRUD operations
- Data model validation
- Flow-based data updates
- Transaction handling

### Business Logic ✅
- ViewModel state management
- Repository pattern implementation
- Data transformation and validation
- Error handling

### User Flows ✅
- Complete expense creation workflow
- Navigation between screens
- Form submission and validation
- Error scenarios and recovery

## Test Patterns and Best Practices

### 1. Arrange-Act-Assert Pattern
```kotlin
@Test
fun addExpense_withValidData_callsRepository() = runTest {
    // Arrange
    val amount = 100.0
    val description = "Test Expense"
    val category = "Food"
    
    // Act
    viewModel.addExpense(amount, description, category)
    
    // Assert
    coVerify { mockRepository.insertExpense(any()) }
}
```

### 2. Compose Testing
```kotlin
@Test
fun dashboardScreen_displaysCorrectly() {
    composeTestRule.setContent {
        ExpenseTrackerTheme {
            DashboardScreen(viewModel = mockViewModel, onAddExpenseClick = {})
        }
    }
    
    composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
}
```

### 3. Database Testing
```kotlin
@Test
fun insertExpense_andRetrieve() = runBlocking {
    val expense = Expense(amount = 100.0, description = "Test", category = "Food", date = Date())
    expenseDao.insertExpense(expense)
    
    val expenses = expenseDao.getAllExpenses().first()
    assertEquals(1, expenses.size)
}
```

### 4. MockK Usage
```kotlin
@Before
fun setup() {
    mockRepository = mockk(relaxed = true)
    coEvery { mockRepository.allExpenses } returns MutableStateFlow(testExpenses)
    viewModel = ExpenseViewModel(mockRepository)
}
```

## Continuous Integration

### GitHub Actions (Recommended)
```yaml
name: Android CI
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - name: Run unit tests
        run: ./gradlew testDebugUnitTest
      - name: Run instrumentation tests
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 29
          script: ./gradlew connectedDebugAndroidTest
```

## Test Reports

After running tests, reports are generated in:
- **Unit Tests**: `app/build/reports/tests/testDebugUnitTest/index.html`
- **Instrumentation Tests**: `app/build/reports/androidTests/connected/index.html`

## Troubleshooting

### Common Issues

1. **Build Failures**: Ensure JVM target is set to 11
2. **MockK Issues**: Use `relaxed = true` for simple mocking
3. **Compose Test Issues**: Ensure proper theme wrapping
4. **Database Tests**: Use in-memory database for testing

### Device Requirements
- **Minimum API Level**: 29
- **Recommended**: Use emulator for consistent results
- **Physical Device**: Ensure USB debugging is enabled

## Adding New Tests

### 1. UI Component Test
```kotlin
@RunWith(AndroidJUnit4::class)
class NewComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun newComponent_displaysCorrectly() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                NewComponent()
            }
        }
        // Add assertions
    }
}
```

### 2. Database Test
```kotlin
@Test
fun newDatabaseOperation_worksCorrectly() = runBlocking {
    // Arrange
    val testData = createTestData()
    
    // Act
    dao.performOperation(testData)
    
    // Assert
    val result = dao.getResult().first()
    assertEquals(expectedResult, result)
}
```

### 3. ViewModel Test
```kotlin
@Test
fun newViewModelFunction_behavesCorrectly() = runTest {
    // Arrange
    setupMocks()
    
    // Act
    viewModel.performAction()
    
    // Assert
    coVerify { mockRepository.expectedCall() }
}
```

## Test Maintenance

- **Regular Updates**: Keep test dependencies up to date
- **Coverage Monitoring**: Aim for >80% code coverage
- **Performance**: Keep test execution time under 5 minutes
- **Reliability**: Tests should be deterministic and not flaky

---

For more information about Android testing, refer to the [official documentation](https://developer.android.com/training/testing).
