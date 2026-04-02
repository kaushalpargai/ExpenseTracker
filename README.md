# Expense Tracker - Enhanced with AI

A modern expense tracking application built with Android Jetpack Compose featuring category-based tracking, visual analytics, and AI-powered insights.

## ✨ New Features

### 🏠 Home Screen
- **Quick Add Expense**: Inline form to add expenses with amount, description, and category
- **Category Summaries**: Real-time category totals with progress bars and percentages
- **Recent Expenses**: List of your latest transactions
- **Smart Categories**: 8 predefined categories with color coding

### 📊 Category Analytics
- **Pie Chart Visualization**: Interactive donut chart showing expense distribution
- **Category Breakdown**: Detailed view of each category with totals and percentages
- **Expense Count**: Track number of transactions per category

### 🤖 AI Insights (Powered by Gemini)
- **Weekly Comparison**: AI analysis of your weekly spending vs monthly average
- **Monthly Trends**: Insights into your monthly spending patterns
- **Smart Recommendations**: 3-5 personalized tips to improve spending habits
- **Key Insights**: AI-identified patterns in your expenses

## 🚀 Setup Instructions

### Prerequisites
- Android Studio (latest version)
- Android SDK 29 or higher
- Google Gemini API key (free tier available)

### Getting Your Gemini API Key

1. Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy your API key

### Configuration

1. Open the project in Android Studio
2. Locate the `local.properties` file in the project root
3. Add your Gemini API key:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
4. Sync the project with Gradle files
5. Run the app on an emulator or physical device

> **Note**: The app will work without an API key, but AI Insights feature will be disabled.

## 📱 Navigation

- **Home** (Tab 1): Add expenses and view category summaries
- **Categories** (Tab 2): Visual analytics and category breakdown
- **AI Insights** (Tab 3): Get AI-powered spending recommendations
- **Account** (Tab 4): Coming soon

## 🎨 Technologies Used

- **Kotlin** - Primary language
- **Jetpack Compose** - Modern UI toolkit
- **Room Database** - Local data persistence
- **Material Design 3** - UI components and theming
- **Coroutines & Flow** - Asynchronous programming
- **ViewModel** - State management
- **Google Gemini AI** - Expense analysis and recommendations
- **Canvas API** - Custom charts and visualizations

## 🎯 Categories

The app includes 8 predefined categories:
- 🍔 Food
- 🚗 Transport
- 🛍️ Shopping
- 🎬 Entertainment
- 💡 Bills
- 🏥 Healthcare
- 📚 Education
- 📦 Other

Each category has a unique color for easy identification.

## 💡 Usage Tips

1. **Add Expenses Regularly**: The more data you add, the better AI insights you'll get
2. **Use Accurate Categories**: Proper categorization helps AI provide relevant recommendations
3. **Check AI Insights Weekly**: Get fresh analysis by tapping "Analyze" in the AI Insights screen
4. **Review Category Analytics**: Use the pie chart to identify your biggest spending areas

## 🔒 Privacy

- All expense data is stored locally on your device
- AI analysis is performed via secure API calls to Google Gemini
- No personal data is stored on external servers
- Your API key is stored locally and never shared

## 🐛 Troubleshooting

**AI Insights not working?**
- Ensure you've added your Gemini API key to `local.properties`
- Check your internet connection
- Verify you have at least a few expenses added

**App crashes on startup?**
- Clean and rebuild the project
- Invalidate caches and restart Android Studio
- Check that all dependencies are properly synced

## 📄 License

This project is open source and available for educational purposes.

## 🙏 Acknowledgments

- Google Gemini AI for powering intelligent expense analysis
- Material Design 3 for beautiful UI components
- Jetpack Compose team for the modern UI framework
