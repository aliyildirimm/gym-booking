import React, { useState, useEffect } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import { useClassDetails, useBookingSubmit } from '../../../hooks';
import { PageLayout, LoadingSpinner } from '../../../components';
import {
  ClassLoadingError,
  BookingSuccess,
  BookingFormContent,
} from '../components';

const BookingForm: React.FC = () => {
  const { classId } = useParams<{ classId: string }>();
  const { state: classState, fetchClass } = useClassDetails();
  const { state: bookingState, submitBooking } = useBookingSubmit();
  const [userName, setUserName] = useState('');
  const history = useHistory();

  // Fetch class details when component mounts or classId changes
  useEffect(() => {
    if (classId) {
      fetchClass(classId);
    }
  }, [classId]);

  const navigateToHome = () => {
    history.push('/');
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const name = userName?.trim();
    if (!name || !classId) return;

    await submitBooking({ classId: Number(classId), userName: name });
  };

  // Determine page title based on state
  const getTitle = () => {
    if (bookingState.status === 'success') return 'Booking Confirmed';
    if (classState.status === 'success') return `Book: ${classState.data.name}`;
    return 'Book Class';
  };

  // Determine page content based on state
  const getContent = () => {
    if (classState.status === 'loading' || classState.status === 'idle') {
      return <LoadingSpinner />;
    }

    if (classState.status === 'error') {
      return (
        <ClassLoadingError
          error={classState.error}
          onBackToClasses={navigateToHome}
        />
      );
    }

    if (bookingState.status === 'success') {
      return <BookingSuccess onBackToClasses={navigateToHome} />;
    }

    // At this point, classState.status === 'success', so data exists
    return (
      <BookingFormContent
        classData={{
          availableSpots: classState.data.availableSpots,
          totalCapacity: classState.data.totalCapacity,
        }}
        userName={userName}
        onUserNameChange={setUserName}
        onSubmit={handleSubmit}
        bookingError={
          bookingState.status === 'error' ? bookingState.error : undefined
        }
        isSubmitting={bookingState.status === 'loading'}
      />
    );
  };

  return (
    <PageLayout
      title={getTitle()}
      showBackButton={classState.status !== 'loading' && classState.status !== 'idle'}
      backHref="/"
    >
      {getContent()}
    </PageLayout>
  );
};

export default BookingForm;
