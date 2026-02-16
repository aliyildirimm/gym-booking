# Implementation Plan: Admin and Customer Pages

## Context

The gym booking system currently has a fully functional backend with Class Service and Booking Service. The frontend has customer-facing pages to view classes and create bookings, but there's no UI for administrators to create new gym classes.

**Problem**: Currently, gym classes can only be created via direct API calls (curl/Postman). This is not user-friendly for non-technical administrators.

**Solution**: Add an admin section with a simple form to create gym classes, while keeping the existing customer flow intact. No authentication needed for MVP - users will access admin and customer pages via different URLs.

**Outcome**: Administrators can create classes via `/admin/create-class`, and customers continue to browse and book classes via `/` and `/book/:classId`.

---

## Route Structure

```
/                        → Customer: Browse all classes (existing)
/book/:classId           → Customer: Book a class (existing)
/admin                   → Admin: Landing page with admin features
/admin/create-class      → Admin: Form to create new gym class
```

**Navigation**: Users access pages via direct URLs (no menu/tabs needed for MVP).

---

## Implementation Steps

### 1. Add API Layer for Class Creation

**Files to modify:**
- `/home/aliyildirim/gym-booking/frontend/src/types/api.ts`
- `/home/aliyildirim/gym-booking/frontend/src/api.ts`

**Changes:**
1. Add `CreateClassPayload` interface to `types/api.ts`:
   ```typescript
   export interface CreateClassPayload {
     name: string;
     capacity: number;
   }
   ```

2. Add `createClass()` function to `api.ts`:
   ```typescript
   export function createClass(
     payload: CreateClassPayload
   ): Promise<AxiosResponse<GymClass>> {
     return classApi.post<GymClass>('/classes', payload);
   }
   ```

**Pattern**: Mirrors existing `createBooking()` function (line 18-22 in api.ts).

---

### 2. Create Hook for Class Submission

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/hooks/useClassCreate.ts`

**Pattern**: Mirror `useBookingSubmit.ts` exactly (same state management, error handling).

**Implementation:**
```typescript
import { useState } from 'react';
import { createClass } from '../api';
import type { CreateClassPayload } from '../types/api';
import type { AsyncState } from '../types/async';

interface UseClassCreateResult {
  state: AsyncState;
  submitClass: (payload: CreateClassPayload) => Promise<void>;
  reset: () => void;
}

export function useClassCreate(): UseClassCreateResult {
  const [state, setState] = useState<AsyncState>({ status: 'idle' });

  const submitClass = async (payload: CreateClassPayload): Promise<void> => {
    setState({ status: 'loading' });

    try {
      await createClass(payload);
      setState({ status: 'success', data: undefined });
    } catch (err: unknown) {
      const axiosError = err as { response?: { data?: { message?: string; error?: string } }; message?: string };
      const data = axiosError.response?.data;
      const errorMessage = data?.message ?? data?.error ?? axiosError.message ?? 'Failed to create class';
      setState({ status: 'error', error: errorMessage });
    }
  };

  const reset = () => setState({ status: 'idle' });

  return { state, submitClass, reset };
}
```

**File to modify:**
- `/home/aliyildirim/gym-booking/frontend/src/hooks/index.ts` - Add export: `export { useClassCreate } from './useClassCreate';`

---

### 3. Create Admin Feature Structure

**Directories to create:**
```
/home/aliyildirim/gym-booking/frontend/src/features/admin/
/home/aliyildirim/gym-booking/frontend/src/features/admin/pages/
/home/aliyildirim/gym-booking/frontend/src/features/admin/components/
```

---

### 4. Create Form Components (Bottom-Up)

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/features/admin/components/CreateClassFormContent.tsx`

**Pattern**: Mirror `BookingFormContent.tsx` - uses Ionic form components.

**Props:**
```typescript
interface CreateClassFormContentProps {
  className: string;
  capacity: string;
  onClassNameChange: (value: string) => void;
  onCapacityChange: (value: string) => void;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  submitError?: string;
  isSubmitting: boolean;
}
```

**Form fields:**
- Class name: `IonInput` type="text", required
- Capacity: `IonInput` type="number", min="1", required
- Submit button: `PrimaryButton` with loading state

---

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/features/admin/components/ClassCreatedSuccess.tsx`

**Pattern**: Mirror `BookingSuccess.tsx`.

**Props:**
```typescript
interface ClassCreatedSuccessProps {
  onViewClasses: () => void;
  onCreateAnother: () => void;
}
```

**Content:**
- Success message with checkmark icon
- "View All Classes" button → navigates to `/`
- "Create Another Class" button → resets form state

---

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/features/admin/components/index.ts`

```typescript
export { CreateClassFormContent } from './CreateClassFormContent';
export { ClassCreatedSuccess } from './ClassCreatedSuccess';
```

---

### 5. Create Page Components

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/features/admin/pages/CreateClass.tsx`

**Pattern**: Mirror `BookingForm.tsx` structure (lines 11-92).

**State management:**
```typescript
const [className, setClassName] = useState('');
const [capacity, setCapacity] = useState('');
const { state, submitClass, reset } = useClassCreate();
```

**Key methods:**
- `handleSubmit()` - validates inputs, calls `submitClass()`
- `getTitle()` - returns dynamic title based on state
- `getContent()` - returns form or success component based on state

**Conditional rendering:**
- `state.status === 'success'` → Show `ClassCreatedSuccess`
- Otherwise → Show `CreateClassFormContent` with error handling

---

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/features/admin/pages/AdminHome.tsx`

**Simple landing page** with navigation to admin features:

```typescript
import { IonList, IonItem, IonLabel } from '@ionic/react';
import { PageLayout } from '../../../components';

const AdminHome: React.FC = () => (
  <PageLayout title="Admin Dashboard">
    <IonList>
      <IonItem button routerLink="/admin/create-class">
        <IonLabel>
          <h2>Create New Class</h2>
          <p>Add a new gym class to the schedule</p>
        </IonLabel>
      </IonItem>
    </IonList>
  </PageLayout>
);
```

---

**File to create:**
- `/home/aliyildirim/gym-booking/frontend/src/features/admin/index.ts`

```typescript
export { default as AdminHome } from './pages/AdminHome';
export { default as CreateClass } from './pages/CreateClass';
```

---

### 6. Update Routing

**File to modify:**
- `/home/aliyildirim/gym-booking/frontend/src/App.tsx`

**Add imports:**
```typescript
import { AdminHome, CreateClass } from './features/admin';
```

**Add routes** (inside `IonRouterOutlet`):
```typescript
<Route path="/" component={ClassList} exact />
<Route path="/book/:classId" component={BookingForm} />
<Route path="/admin" component={AdminHome} exact />
<Route path="/admin/create-class" component={CreateClass} />
```

---

## Critical Files Reference

**Patterns to follow:**
- `/home/aliyildirim/gym-booking/frontend/src/hooks/useBookingSubmit.ts` - Hook pattern for `useClassCreate`
- `/home/aliyildirim/gym-booking/frontend/src/features/booking/pages/BookingForm.tsx` - Page pattern for `CreateClass`
- `/home/aliyildirim/gym-booking/frontend/src/features/booking/components/BookingFormContent.tsx` - Form component pattern
- `/home/aliyildirim/gym-booking/frontend/src/api.ts` - API function pattern

**Files to modify:**
1. `/home/aliyildirim/gym-booking/frontend/src/types/api.ts`
2. `/home/aliyildirim/gym-booking/frontend/src/api.ts`
3. `/home/aliyildirim/gym-booking/frontend/src/hooks/index.ts`
4. `/home/aliyildirim/gym-booking/frontend/src/App.tsx`

**New files to create:**
1. `/home/aliyildirim/gym-booking/frontend/src/hooks/useClassCreate.ts`
2. `/home/aliyildirim/gym-booking/frontend/src/features/admin/index.ts`
3. `/home/aliyildirim/gym-booking/frontend/src/features/admin/pages/AdminHome.tsx`
4. `/home/aliyildirim/gym-booking/frontend/src/features/admin/pages/CreateClass.tsx`
5. `/home/aliyildirim/gym-booking/frontend/src/features/admin/components/index.ts`
6. `/home/aliyildirim/gym-booking/frontend/src/features/admin/components/CreateClassFormContent.tsx`
7. `/home/aliyildirim/gym-booking/frontend/src/features/admin/components/ClassCreatedSuccess.tsx`

---

## Verification & Testing

### Prerequisites
```bash
# Start all services
docker-compose up -d

# Verify backend is running
curl http://localhost:8081/classes
curl http://localhost:8082/bookings
```

### End-to-End Test Flow

1. **Access admin page:**
   - Navigate to `http://localhost:3000/admin`
   - Verify "Admin Dashboard" title appears
   - Verify "Create New Class" button is visible

2. **Create a new class:**
   - Click "Create New Class" → Should navigate to `/admin/create-class`
   - Enter class name: "Morning Yoga"
   - Enter capacity: "15"
   - Click "Create Class" button
   - Verify loading spinner appears during submission

3. **Verify backend integration:**
   - Open browser DevTools Network tab
   - Check `POST http://localhost:8081/classes` request
   - Verify request body: `{"name":"Morning Yoga","capacity":15}`
   - Verify response: `201 Created` with class data

4. **Verify success flow:**
   - After successful creation, verify success message appears
   - Click "View All Classes" → Should navigate to `/`
   - Verify "Morning Yoga" appears in the class list
   - Verify capacity shows "15 spots available"

5. **Test customer flow with new class:**
   - From customer home (`/`), click on "Morning Yoga"
   - Enter a customer name
   - Submit booking
   - Navigate back to class list
   - Verify "Morning Yoga" now shows "14 spots available"

6. **Test error handling:**
   - Navigate to `/admin/create-class`
   - Leave class name empty, click submit → Should prevent submission
   - Enter class name, set capacity to "0", click submit → Should show error
   - Stop Class Service: `docker-compose stop class-service`
   - Try to create class → Should show error message "Failed to create class"
   - Restart service: `docker-compose start class-service`

7. **Test "Create Another" flow:**
   - Create a class successfully
   - Click "Create Another Class" button
   - Verify form resets to empty state
   - Verify you can create another class immediately

### Success Criteria

✅ Admin can create classes via UI (no curl/Postman needed)
✅ Customers can see newly created classes immediately
✅ Form validates inputs (required fields, min capacity)
✅ Backend errors display user-friendly messages
✅ Success state shows confirmation and navigation options
✅ Routes are clean and RESTful (`/admin/*` prefix)
✅ Follows existing codebase patterns (hooks, components, styling)
