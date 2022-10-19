import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRotasOfertas, NewRotasOfertas } from '../rotas-ofertas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRotasOfertas for edit and NewRotasOfertasFormGroupInput for create.
 */
type RotasOfertasFormGroupInput = IRotasOfertas | PartialWithRequiredKeyOf<NewRotasOfertas>;

type RotasOfertasFormDefaults = Pick<NewRotasOfertas, 'id'>;

type RotasOfertasFormGroupContent = {
  id: FormControl<IRotasOfertas['id'] | NewRotasOfertas['id']>;
  rotas: FormControl<IRotasOfertas['rotas']>;
  ofertas: FormControl<IRotasOfertas['ofertas']>;
};

export type RotasOfertasFormGroup = FormGroup<RotasOfertasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RotasOfertasFormService {
  createRotasOfertasFormGroup(rotasOfertas: RotasOfertasFormGroupInput = { id: null }): RotasOfertasFormGroup {
    const rotasOfertasRawValue = {
      ...this.getFormDefaults(),
      ...rotasOfertas,
    };
    return new FormGroup<RotasOfertasFormGroupContent>({
      id: new FormControl(
        { value: rotasOfertasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rotas: new FormControl(rotasOfertasRawValue.rotas, {
        validators: [Validators.required],
      }),
      ofertas: new FormControl(rotasOfertasRawValue.ofertas, {
        validators: [Validators.required],
      }),
    });
  }

  getRotasOfertas(form: RotasOfertasFormGroup): IRotasOfertas | NewRotasOfertas {
    return form.getRawValue() as IRotasOfertas | NewRotasOfertas;
  }

  resetForm(form: RotasOfertasFormGroup, rotasOfertas: RotasOfertasFormGroupInput): void {
    const rotasOfertasRawValue = { ...this.getFormDefaults(), ...rotasOfertas };
    form.reset(
      {
        ...rotasOfertasRawValue,
        id: { value: rotasOfertasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RotasOfertasFormDefaults {
    return {
      id: null,
    };
  }
}
