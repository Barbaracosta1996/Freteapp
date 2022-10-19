import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPerfil, NewPerfil } from '../perfil.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerfil for edit and NewPerfilFormGroupInput for create.
 */
type PerfilFormGroupInput = IPerfil | PartialWithRequiredKeyOf<NewPerfil>;

type PerfilFormDefaults = Pick<NewPerfil, 'id'>;

type PerfilFormGroupContent = {
  id: FormControl<IPerfil['id'] | NewPerfil['id']>;
  tipoConta: FormControl<IPerfil['tipoConta']>;
  cpf: FormControl<IPerfil['cpf']>;
  cnpj: FormControl<IPerfil['cnpj']>;
  rua: FormControl<IPerfil['rua']>;
  numero: FormControl<IPerfil['numero']>;
  bairro: FormControl<IPerfil['bairro']>;
  cidade: FormControl<IPerfil['cidade']>;
  estado: FormControl<IPerfil['estado']>;
  cep: FormControl<IPerfil['cep']>;
  pais: FormControl<IPerfil['pais']>;
  nome: FormControl<IPerfil['nome']>;
  razaosocial: FormControl<IPerfil['razaosocial']>;
  telefoneComercial: FormControl<IPerfil['telefoneComercial']>;
  user: FormControl<IPerfil['user']>;
};

export type PerfilFormGroup = FormGroup<PerfilFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PerfilFormService {
  createPerfilFormGroup(perfil: PerfilFormGroupInput = { id: null }): PerfilFormGroup {
    const perfilRawValue = {
      ...this.getFormDefaults(),
      ...perfil,
    };
    return new FormGroup<PerfilFormGroupContent>({
      id: new FormControl(
        { value: perfilRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipoConta: new FormControl(perfilRawValue.tipoConta, {
        validators: [Validators.required],
      }),
      cpf: new FormControl(perfilRawValue.cpf, {
        validators: [Validators.minLength(11)],
      }),
      cnpj: new FormControl(perfilRawValue.cnpj, {
        validators: [Validators.minLength(14)],
      }),
      rua: new FormControl(perfilRawValue.rua),
      numero: new FormControl(perfilRawValue.numero),
      bairro: new FormControl(perfilRawValue.bairro),
      cidade: new FormControl(perfilRawValue.cidade),
      estado: new FormControl(perfilRawValue.estado),
      cep: new FormControl(perfilRawValue.cep),
      pais: new FormControl(perfilRawValue.pais),
      nome: new FormControl(perfilRawValue.nome, {
        validators: [Validators.required],
      }),
      razaosocial: new FormControl(perfilRawValue.razaosocial),
      telefoneComercial: new FormControl(perfilRawValue.telefoneComercial),
      user: new FormControl(perfilRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getPerfil(form: PerfilFormGroup): IPerfil | NewPerfil {
    return form.getRawValue() as IPerfil | NewPerfil;
  }

  resetForm(form: PerfilFormGroup, perfil: PerfilFormGroupInput): void {
    const perfilRawValue = { ...this.getFormDefaults(), ...perfil };
    form.reset(
      {
        ...perfilRawValue,
        id: { value: perfilRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PerfilFormDefaults {
    return {
      id: null,
    };
  }
}
