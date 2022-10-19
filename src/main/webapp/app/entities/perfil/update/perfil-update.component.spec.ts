import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PerfilFormService } from './perfil-form.service';
import { PerfilService } from '../service/perfil.service';
import { IPerfil } from '../perfil.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PerfilUpdateComponent } from './perfil-update.component';

describe('Perfil Management Update Component', () => {
  let comp: PerfilUpdateComponent;
  let fixture: ComponentFixture<PerfilUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let perfilFormService: PerfilFormService;
  let perfilService: PerfilService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PerfilUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PerfilUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerfilUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    perfilFormService = TestBed.inject(PerfilFormService);
    perfilService = TestBed.inject(PerfilService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const perfil: IPerfil = { id: 456 };
      const user: IUser = { id: 3437 };
      perfil.user = user;

      const userCollection: IUser[] = [{ id: 32146 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ perfil });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const perfil: IPerfil = { id: 456 };
      const user: IUser = { id: 98812 };
      perfil.user = user;

      activatedRoute.data = of({ perfil });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.perfil).toEqual(perfil);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfil>>();
      const perfil = { id: 123 };
      jest.spyOn(perfilFormService, 'getPerfil').mockReturnValue(perfil);
      jest.spyOn(perfilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfil }));
      saveSubject.complete();

      // THEN
      expect(perfilFormService.getPerfil).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(perfilService.update).toHaveBeenCalledWith(expect.objectContaining(perfil));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfil>>();
      const perfil = { id: 123 };
      jest.spyOn(perfilFormService, 'getPerfil').mockReturnValue({ id: null });
      jest.spyOn(perfilService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfil: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfil }));
      saveSubject.complete();

      // THEN
      expect(perfilFormService.getPerfil).toHaveBeenCalled();
      expect(perfilService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfil>>();
      const perfil = { id: 123 };
      jest.spyOn(perfilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(perfilService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
