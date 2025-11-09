'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@heroui/button';
import { Input } from '@heroui/input';
import { Card, CardBody, CardHeader } from '@heroui/card';
import { Link } from '@heroui/link';
import { useAuth } from '@/contexts/AuthContext';
import { title } from '@/components/primitives';

export default function RegisterPage() {
  const router = useRouter();
  const { register } = useAuth();

  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [serverError, setServerError] = useState('');

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = 'El nom és obligatori';
    }

    if (!formData.surname.trim()) {
      newErrors.surname = 'Els cognoms són obligatoris';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'El correu electrònic és obligatori';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'El correu electrònic no és vàlid';
    }

    if (!formData.username.trim()) {
      newErrors.username = 'El nom d\'usuari és obligatori';
    } else if (formData.username.length < 3 || formData.username.length > 50) {
      newErrors.username = 'El nom d\'usuari ha de tenir entre 3 i 50 caràcters';
    }

    if (!formData.password) {
      newErrors.password = 'La contrasenya és obligatòria';
    } else if (formData.password.length < 6) {
      newErrors.password = 'La contrasenya ha de tenir almenys 6 caràcters';
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Les contrasenyes no coincideixen';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setServerError('');

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      await register({
        name: formData.name,
        surname: formData.surname,
        email: formData.email,
        username: formData.username,
        password: formData.password,
      });

      router.push('/');
    } catch (error) {
      setServerError(
        error instanceof Error ? error.message : 'Error en el registre. Si us plau, torna-ho a intentar.'
      );
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({ ...prev, [field]: e.target.value }));
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: '' }));
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-200px)] py-12">
      <Card className="w-full max-w-md">
        <CardHeader className="flex flex-col gap-1 items-center pb-6 pt-8">
          <h1 className={title({ size: 'sm' })}>
            Registra't a <span className={title({ color: 'blue', size: 'sm' })}>ProTube</span>
          </h1>
          <p className="text-small text-default-500">Crea el teu compte per començar</p>
        </CardHeader>
        <CardBody>
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <Input
              label="Nom"
              placeholder="Introdueix el teu nom"
              value={formData.name}
              onChange={handleChange('name')}
              isInvalid={!!errors.name}
              errorMessage={errors.name}
              isRequired
              variant="bordered"
            />

            <Input
              label="Cognoms"
              placeholder="Introdueix els teus cognoms"
              value={formData.surname}
              onChange={handleChange('surname')}
              isInvalid={!!errors.surname}
              errorMessage={errors.surname}
              isRequired
              variant="bordered"
            />

            <Input
              label="Correu electrònic"
              placeholder="correu@example.com"
              type="email"
              value={formData.email}
              onChange={handleChange('email')}
              isInvalid={!!errors.email}
              errorMessage={errors.email}
              isRequired
              variant="bordered"
            />

            <Input
              label="Nom d'usuari"
              placeholder="Tria un nom d'usuari"
              value={formData.username}
              onChange={handleChange('username')}
              isInvalid={!!errors.username}
              errorMessage={errors.username}
              isRequired
              variant="bordered"
            />

            <Input
              label="Contrasenya"
              placeholder="Mínim 6 caràcters"
              type="password"
              value={formData.password}
              onChange={handleChange('password')}
              isInvalid={!!errors.password}
              errorMessage={errors.password}
              isRequired
              variant="bordered"
            />

            <Input
              label="Confirma la contrasenya"
              placeholder="Repeteix la contrasenya"
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange('confirmPassword')}
              isInvalid={!!errors.confirmPassword}
              errorMessage={errors.confirmPassword}
              isRequired
              variant="bordered"
            />

            {serverError && (
              <div className="bg-danger-50 text-danger border border-danger-200 rounded-lg p-3 text-sm">
                {serverError}
              </div>
            )}

            <Button
              type="submit"
              color="primary"
              size="lg"
              isLoading={isLoading}
              className="w-full"
            >
              Registrar-se
            </Button>

            <p className="text-center text-small">
              Ja tens compte?{' '}
              <Link href="/login" size="sm" className="font-semibold">
                Inicia sessió
              </Link>
            </p>
          </form>
        </CardBody>
      </Card>
    </div>
  );
}

