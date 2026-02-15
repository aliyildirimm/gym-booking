import React, { useEffect, useState } from 'react';
import { IonItem, IonLabel, IonInput, IonButton } from '@ionic/react';
import { useParams, useNavigate } from 'react-router-dom';
import { getClass, createBooking } from '../api';
import type { GymClass } from '../types/api';
import {
  PageLayout,
  LoadingSpinner,
  ErrorMessage,
  PrimaryButton,
} from '../components';

const BookingForm: React.FC = () => {
  const { classId } = useParams<{ classId: string }>();
  const navigate = useNavigate();
  const [gymClass, setGymClass] = useState<GymClass | null>(null);
  const [userName, setUserName] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (classId == null || classId === '') {
      setLoading(false);
      setError('Missing class');
      return;
    }
    getClass(classId)
      .then((res) => {
        setGymClass(res.data);
        setError(null);
      })
      .catch((err) => {
        const data = err.response?.data as { message?: string } | undefined;
        setError(data?.message ?? err.message ?? 'Class not found');
        setGymClass(null);
      })
      .finally(() => setLoading(false));
  }, [classId]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const name = userName?.trim();
    if (!name) {
      setError('Please enter your name');
      return;
    }
    if (classId == null) return;
    setSubmitting(true);
    setError(null);
    createBooking({ classId: Number(classId), userName: name })
      .then(() => setSuccess(true))
      .catch((err) => {
        const data = err.response?.data as { message?: string; error?: string } | undefined;
        setError(data?.message ?? data?.error ?? err.message ?? 'Booking failed');
      })
      .finally(() => setSubmitting(false));
  };

  if (loading) {
    return (
      <PageLayout title="Book Class">
        <LoadingSpinner />
      </PageLayout>
    );
  }

  if (error != null && gymClass == null) {
    return (
      <PageLayout title="Book Class" showBackButton backHref="/">
        <ErrorMessage message={error} className="ion-color-danger" />
        <IonButton expand="block" onClick={() => navigate('/')}>
          Back to classes
        </IonButton>
      </PageLayout>
    );
  }

  if (success) {
    return (
      <PageLayout title="Book Class">
        <p className="ion-text-center ion-padding">Booking confirmed!</p>
        <PrimaryButton onClick={() => navigate('/')}>
          Back to classes
        </PrimaryButton>
      </PageLayout>
    );
  }

  return (
    <PageLayout title={`Book: ${gymClass?.name ?? 'Class'}`} showBackButton backHref="/">
      {gymClass != null && (
        <IonItem lines="none">
          <IonLabel>
            <p>
              {gymClass.availableSpots} / {gymClass.totalCapacity} spots available
            </p>
          </IonLabel>
        </IonItem>
      )}
      <form onSubmit={handleSubmit}>
        <IonItem>
          <IonLabel position="stacked">Your name</IonLabel>
          <IonInput
            type="text"
            value={userName}
            onIonInput={(e) => setUserName((e.detail.value as string) ?? '')}
            placeholder="Enter name"
            required
          />
        </IonItem>
        {error != null && (
          <ErrorMessage
            message={error}
            className="ion-color-danger ion-padding-start"
          />
        )}
        <PrimaryButton type="submit" loading={submitting} disabled={submitting}>
          Book
        </PrimaryButton>
      </form>
    </PageLayout>
  );
};

export default BookingForm;
