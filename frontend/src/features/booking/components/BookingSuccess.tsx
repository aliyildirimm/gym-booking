import React from 'react';
import { PrimaryButton } from '../../../components';

interface BookingSuccessProps {
  onBackToClasses: () => void;
}

export const BookingSuccess: React.FC<BookingSuccessProps> = ({
  onBackToClasses,
}) => (
  <>
    <p className="ion-text-center ion-padding">Booking confirmed!</p>
    <PrimaryButton onClick={onBackToClasses}>Back to classes</PrimaryButton>
  </>
);
