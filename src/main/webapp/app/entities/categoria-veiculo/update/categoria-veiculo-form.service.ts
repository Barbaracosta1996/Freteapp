import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategoriaVeiculo, NewCategoriaVeiculo } from '../categoria-veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoriaVeiculo for edit and NewCategoriaVeiculoFormGroupInput for create.
 */
type CategoriaVeiculoFormGroupInput = ICategoriaVeiculo | PartialWithRequiredKeyOf<NewCategoriaVeiculo>;

type CategoriaVeiculoFormDefaults = Pick<NewCategoriaVeiculo, 'id'>;

type CategoriaVeiculoFormGroupContent = {
  id: FormControl<ICategoriaVeiculo['id'] | NewCategoriaVeiculo['id']>;
  nome: FormControl<ICategoriaVeiculo['nome']>;
};

export type CategoriaVeiculoFormGroup = FormGroup<CategoriaVeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriaVeiculoFormService {
  createCategoriaVeiculoFormGroup(categoriaVeiculo: CategoriaVeiculoFormGroupInput = { id: null }): CategoriaVeiculoFormGroup {
    const categoriaVeiculoRawValue = {
      ...this.getFormDefaults(),
      ...categoriaVeiculo,
    };
    return new FormGroup<CategoriaVeiculoFormGroupContent>({
      id: new FormControl(
        { value: categoriaVeiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(categoriaVeiculoRawValue.nome, {
        validators: [Validators.required],
      }),
    });
  }

  getCategoriaVeiculo(form: CategoriaVeiculoFormGroup): ICategoriaVeiculo | NewCategoriaVeiculo {
    return form.getRawValue() as ICategoriaVeiculo | NewCategoriaVeiculo;
  }

  resetForm(form: CategoriaVeiculoFormGroup, categoriaVeiculo: CategoriaVeiculoFormGroupInput): void {
    const categoriaVeiculoRawValue = { ...this.getFormDefaults(), ...categoriaVeiculo };
    form.reset(
      {
        ...categoriaVeiculoRawValue,
        id: { value: categoriaVeiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriaVeiculoFormDefaults {
    return {
      id: null,
    };
  }
}
