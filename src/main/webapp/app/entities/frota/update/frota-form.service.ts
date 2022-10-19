import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFrota, NewFrota } from '../frota.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFrota for edit and NewFrotaFormGroupInput for create.
 */
type FrotaFormGroupInput = IFrota | PartialWithRequiredKeyOf<NewFrota>;

type FrotaFormDefaults = Pick<NewFrota, 'id'>;

type FrotaFormGroupContent = {
  id: FormControl<IFrota['id'] | NewFrota['id']>;
  nome: FormControl<IFrota['nome']>;
  modelo: FormControl<IFrota['modelo']>;
  marca: FormControl<IFrota['marca']>;
  ano: FormControl<IFrota['ano']>;
  tipoCategoria: FormControl<IFrota['tipoCategoria']>;
  obsCategoriaOutro: FormControl<IFrota['obsCategoriaOutro']>;
  perfil: FormControl<IFrota['perfil']>;
  categoriaVeiculo: FormControl<IFrota['categoriaVeiculo']>;
};

export type FrotaFormGroup = FormGroup<FrotaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FrotaFormService {
  createFrotaFormGroup(frota: FrotaFormGroupInput = { id: null }): FrotaFormGroup {
    const frotaRawValue = {
      ...this.getFormDefaults(),
      ...frota,
    };
    return new FormGroup<FrotaFormGroupContent>({
      id: new FormControl(
        { value: frotaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(frotaRawValue.nome, {
        validators: [Validators.required],
      }),
      modelo: new FormControl(frotaRawValue.modelo, {
        validators: [Validators.required],
      }),
      marca: new FormControl(frotaRawValue.marca),
      ano: new FormControl(frotaRawValue.ano, {
        validators: [Validators.required, Validators.maxLength(4)],
      }),
      tipoCategoria: new FormControl(frotaRawValue.tipoCategoria, {
        validators: [Validators.required],
      }),
      obsCategoriaOutro: new FormControl(frotaRawValue.obsCategoriaOutro),
      perfil: new FormControl(frotaRawValue.perfil, {
        validators: [Validators.required],
      }),
      categoriaVeiculo: new FormControl(frotaRawValue.categoriaVeiculo, {
        validators: [Validators.required],
      }),
    });
  }

  getFrota(form: FrotaFormGroup): IFrota | NewFrota {
    return form.getRawValue() as IFrota | NewFrota;
  }

  resetForm(form: FrotaFormGroup, frota: FrotaFormGroupInput): void {
    const frotaRawValue = { ...this.getFormDefaults(), ...frota };
    form.reset(
      {
        ...frotaRawValue,
        id: { value: frotaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FrotaFormDefaults {
    return {
      id: null,
    };
  }
}
