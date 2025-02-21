
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginComponent } from './login.component';
import {LoginService} from "../../services/login.service";

describe('LoginService', () => {
  let service: LoginService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LoginService],
    });

    service = TestBed.inject(LoginService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  describe('#setToken', () => {
    it('should save the token in localStorage and update the authStatus', () => {
      const token = 'testToken123';
      spyOn(localStorage, 'setItem'); // Espía "setItem" de localStorage
      const authStatusSpy = spyOn(service.authStatus, 'next');

      service.setToken(token);

      expect(localStorage.setItem).toHaveBeenCalledWith(service.localToken, token);
      expect(authStatusSpy).toHaveBeenCalledWith(true);
    });
  });

  describe('#getToken', () => {
    it('should retrieve the token from localStorage', () => {
      const storedToken = 'storedToken123';
      spyOn(localStorage, 'getItem').and.returnValue(storedToken);

      const token = service.getToken();

      expect(localStorage.getItem).toHaveBeenCalledWith(service.localToken);
      expect(token).toBe(storedToken);
    });
  });

  describe('#isLoggedIn', () => {
    it('should return true if a token exists in localStorage', () => {
      spyOn(localStorage, 'getItem').and.returnValue('someToken');
      expect(service.isLoggedIn()).toBeTrue();
    });

    it('should return false if no token exists in localStorage', () => {
      spyOn(localStorage, 'getItem').and.returnValue(null);
      expect(service.isLoggedIn()).toBeFalse();
    });
  });

  describe('#logout', () => {
    it('should remove the token from localStorage, update authStatus, and navigate to login', () => {
      spyOn(localStorage, 'removeItem');
      spyOn(service.authStatus, 'next');
      const routerSpy = spyOn(service._router, 'navigate');

      service.logout();

      expect(localStorage.removeItem).toHaveBeenCalledWith(service.localToken);
      expect(service.authStatus.next).toHaveBeenCalledWith(false);
      expect(routerSpy).toHaveBeenCalledWith(['login']);
    });
  });
});import { ComponentFixture, TestBed } from '@angular/core/testing';




describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
