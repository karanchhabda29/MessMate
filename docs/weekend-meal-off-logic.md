# Weekend & Sunday Meal-Off Logic

## Problem Statement

1. **Sunday Special Meal:** On Sundays, there is only one special meal (lunch) and dinner is off. This single meal is equivalent to 2 normal meals. Users should not be able to take only lunch OR only dinner off on Sundays - it's all or nothing.

2. **Weekend Restriction:** Users should not be able to apply meal-offs for weekends only (Saturday & Sunday). Custom meal-offs must include at least one working day (Monday-Friday).

## Solution Overview

### 1. Daily Meal-Off (Today's Off)

Users can only set/cancel daily meal-offs on weekdays (Monday-Friday).

- **Endpoint:** `POST /meal-off/lunch`, `POST /meal-off/dinner`, etc.
- **Validation:** If today is Saturday or Sunday, reject the request with error: "Daily meal-offs cannot be applied on weekends"
- **Deadline:** 
  - Weekdays: Existing deadlines apply (Lunch: 8:00 AM, Dinner: 4:00 PM)
  - N/A for weekends (rejected upfront)

### 2. Custom Meal-Off

Users can apply custom meal-offs for date ranges, but must follow these rules:

#### Rule A: Must Include At Least One Weekday
- A custom meal-off spanning only Saturday and Sunday (e.g., Saturday Lunch to Sunday Dinner) is invalid.
- **Error:** "Custom meal-offs must include at least one working day (Monday-Friday)"

#### Rule B: Sunday Auto-Correction
- On Sundays, there is no distinction between Lunch and Dinner - it's one combined meal.
- Instead of rejecting requests with incorrect Sunday meal selection, the backend **auto-corrects**:
  - If `startDate` is Sunday → `startMeal` is automatically set to `LUNCH`
  - If `endDate` is Sunday → `endMeal` is automatically set to `DINNER`
- This ensures the scheduler treats Sunday as a full day off (2 meals saved).

## Implementation Details

### File Changes
- **Service:** `MealOffService.java`
  - Add `isWeekend()` helper method
  - Add `containsWeekday()` helper method  
  - Modify `setLunchOff()`, `setDinnerOff()`, `cancelLunchOff()`, `cancelDinnerOff()` to check for weekends
  - Modify `setCustomMealOff()` to:
    1. Validate that at least one weekday exists in the range
    2. Auto-correct Sunday meal selections

### Validation Flow (Custom Meal-Off)

```
User sends request with startDate, endDate, startMeal, endMeal
                    │
                    ▼
        Check: Does date range include at least one weekday?
                    │
           ┌───────┴───────┐
           │               │
          NO             YES
           │               │
           ▼               ▼
    REJECT:        Auto-Correct Sunday Meals
    "Must include      │
    at least one       ▼
    working day"   Check each date
                    │
           ┌────────┼────────┐
           │        │        │
        START   END     NEITHER
        IS      IS      (No change)
        SUNDAY  SUNDAY
           │        │
           ▼        ▼
    set startMeal   set endMeal
    to LUNCH        to DINNER
                    │
                    ▼
            Save to Database
```

### Scheduler Behavior (Unchanged)

The existing schedulers (`MealOffScheduler` and `SubscriptionScheduler`) work without modification because:

1. **Custom Offs:** With auto-correction, Sunday is always saved as `LUNCH` start and `DINNER` end. The scheduler correctly sets both `lunch=true` and `dinner=true` for the user on Sunday.

2. **Daily Offs:** Weekend daily offs are blocked entirely, so there's no scenario where only one meal is marked off on a Sunday.

## Edge Cases

| Scenario | Expected Behavior |
|----------|-------------------|
| Set lunch off on Saturday | Reject: "Daily meal-offs cannot be applied on weekends" |
| Set dinner off on Sunday | Reject: "Daily meal-offs cannot be applied on weekends" |
| Custom off: Saturday Lunch to Sunday Dinner | Reject: "Must include at least one working day" |
| Custom off: Friday Dinner to Monday Lunch | ✅ Valid (includes Friday) |
| Custom off: Monday Lunch to Sunday Dinner | ✅ Valid, auto-corrects to Monday Lunch - Sunday Dinner |
| Custom off: Sunday Lunch to Tuesday Dinner | ✅ Valid, auto-corrects startMeal to Lunch |
