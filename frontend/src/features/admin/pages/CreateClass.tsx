import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useClassCreate } from '../../../hooks';
import { PageLayout } from '../../../components';
import { ClassCreatedSuccess, CreateClassFormContent } from '../components';

const CreateClass: React.FC = () => {
  const [className, setClassName] = useState('');
  const [capacity, setCapacity] = useState('');
  const { state, submitClass, reset } = useClassCreate();
  const history = useHistory();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const name = className?.trim();
    const cap = capacity?.trim();
    if (!name || !cap) return;

    const capacityNum = Number(cap);
    if (capacityNum < 1) return;

    await submitClass({ name, capacity: capacityNum });
  };

  const navigateToClasses = () => {
    history.push('/');
  };

  const handleCreateAnother = () => {
    setClassName('');
    setCapacity('');
    reset();
  };

  const getTitle = () => {
    if (state.status === 'success') return 'Class Created';
    return 'Create New Class';
  };

  const getContent = () => {
    if (state.status === 'success') {
      return (
        <ClassCreatedSuccess
          onViewClasses={navigateToClasses}
          onCreateAnother={handleCreateAnother}
        />
      );
    }

    return (
      <CreateClassFormContent
        className={className}
        capacity={capacity}
        onClassNameChange={setClassName}
        onCapacityChange={setCapacity}
        onSubmit={handleSubmit}
        submitError={state.status === 'error' ? state.error : undefined}
        isSubmitting={state.status === 'loading'}
      />
    );
  };

  return (
    <PageLayout title={getTitle()} showBackButton backHref="/admin">
      {getContent()}
    </PageLayout>
  );
};

export default CreateClass;
