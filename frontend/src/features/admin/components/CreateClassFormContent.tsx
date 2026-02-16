import React from 'react';
import { IonItem, IonLabel, IonInput } from '@ionic/react';
import { ErrorMessage, PrimaryButton } from '../../../components';

interface CreateClassFormContentProps {
  className: string;
  capacity: string;
  onClassNameChange: (value: string) => void;
  onCapacityChange: (value: string) => void;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  submitError?: string;
  isSubmitting: boolean;
}

export const CreateClassFormContent: React.FC<CreateClassFormContentProps> = ({
  className,
  capacity,
  onClassNameChange,
  onCapacityChange,
  onSubmit,
  submitError,
  isSubmitting,
}) => (
  <form onSubmit={onSubmit}>
    <IonItem>
      <IonLabel position="stacked">Class name</IonLabel>
      <IonInput
        type="text"
        value={className}
        onIonInput={(e) => onClassNameChange((e.detail.value as string) ?? '')}
        placeholder="Enter class name"
        required
      />
    </IonItem>
    <IonItem>
      <IonLabel position="stacked">Capacity</IonLabel>
      <IonInput
        type="number"
        value={capacity}
        onIonInput={(e) => onCapacityChange((e.detail.value as string) ?? '')}
        placeholder="Enter capacity"
        min="1"
        required
      />
    </IonItem>
    {submitError && (
      <ErrorMessage
        message={submitError}
        className="ion-color-danger ion-padding-start"
      />
    )}
    <PrimaryButton type="submit" loading={isSubmitting} disabled={isSubmitting}>
      Create Class
    </PrimaryButton>
  </form>
);
