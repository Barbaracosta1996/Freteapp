import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPerfilAnexos, NewPerfilAnexos } from '../perfil-anexos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerfilAnexos for edit and NewPerfilAnexosFormGroupInput for create.
 */
type PerfilAnexosFormGroupInput = IPerfilAnexos | PartialWithRequiredKeyOf<NewPerfilAnexos>;

type PerfilAnexosFormDefaults = Pick<NewPerfilAnexos, 'id'>;

type PerfilAnexosFormGroupContent = {
  id: FormControl<IPerfilAnexos['id'] | NewPerfilAnexos['id']>;
  dataPostagem: FormControl<IPerfilAnexos['dataPostagem']>;
  tipoDocumento: FormControl<IPerfilAnexos['tipoDocumento']>;
  descricao: FormControl<IPerfilAnexos['descricao']>;
  urlFile: FormControl<IPerfilAnexos['urlFile']>;
  urlFileContentType: FormControl<IPerfilAnexos['urlFileContentType']>;
  perfil: FormControl<IPerfilAnexos['perfil']>;
};

export type PerfilAnexosFormGroup = FormGroup<PerfilAnexosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PerfilAnexosFormService {
  createPerfilAnexosFormGroup(perfilAnexos: PerfilAnexosFormGroupInput = { id: null }): PerfilAnexosFormGroup {
    const perfilAnexosRawValue = {
      ...this.getFormDefaults(),
      ...perfilAnexos,
    };
    return new FormGroup<PerfilAnexosFormGroupContent>({
      id: new FormControl(
        { value: perfilAnexosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataPostagem: new FormControl(perfilAnexosRawValue.dataPostagem, {
        validators: [Validators.required],
      }),
      tipoDocumento: new FormControl(perfilAnexosRawValue.tipoDocumento, {
        validators: [Validators.required],
      }),
      descricao: new FormControl(perfilAnexosRawValue.descricao),
      urlFile: new FormControl(perfilAnexosRawValue.urlFile),
      urlFileContentType: new FormControl(perfilAnexosRawValue.urlFileContentType),
      perfil: new FormControl(perfilAnexosRawValue.perfil, {
        validators: [Validators.required],
      }),
    });
  }

  getPerfilAnexos(form: PerfilAnexosFormGroup): IPerfilAnexos | NewPerfilAnexos {
    return form.getRawValue() as IPerfilAnexos | NewPerfilAnexos;
  }

  resetForm(form: PerfilAnexosFormGroup, perfilAnexos: PerfilAnexosFormGroupInput): void {
    const perfilAnexosRawValue = { ...this.getFormDefaults(), ...perfilAnexos };
    form.reset(
      {
        ...perfilAnexosRawValue,
        id: { value: perfilAnexosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PerfilAnexosFormDefaults {
    return {
      id: null,
    };
  }
}
