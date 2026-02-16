import React from 'react';
import { IonItem, IonLabel, IonInput } from '@ionic/react';
import { ErrorMessage, PrimaryButton } from '../../../components';

interface BookingFormContentProps {
  classData: {
    availableSpots: number;
    totalCapacity: number;
  };
  userName: string;
  onUserNameChange: (name: string) => void;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  bookingError?: string;
  isSubmitting: boolean;
}

export const BookingFormContent: React.FC<BookingFormContentProps> = ({
  classData,
  userName,
  onUserNameChange,
  onSubmit,
  bookingError,
  isSubmitting,
}) => (
  <>
    <IonItem lines="none">
      <IonLabel>
        <p>
          {classData.availableSpots} / {classData.totalCapacity} spots available
        </p>
      </IonLabel>
    </IonItem>
    <form onSubmit={onSubmit}>
      <IonItem>
        <IonLabel position="stacked">Your name</IonLabel>
        <IonInput
          type="text"
          value={userName}
          onIonInput={(e) => onUserNameChange((e.detail.value as string) ?? '')}
          placeholder="Enter name"
          required
        />
      </IonItem>
      {bookingError && (
        <ErrorMessage
          message={bookingError}
          className="ion-color-danger ion-padding-start"
        />
      )}
      <PrimaryButton type="submit" loading={isSubmitting} disabled={isSubmitting}>
        Book
      </PrimaryButton>
    </form>
  </>
);
