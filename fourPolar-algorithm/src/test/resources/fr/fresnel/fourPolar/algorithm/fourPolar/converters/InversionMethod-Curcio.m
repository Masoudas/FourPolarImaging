% Import data from file
I_135 = I135_red;
I_45 = I45_red;
I_90 = I90;
I_0 = I0;

%Initialising angles arrays 

Rho=zeros(length(I_0),1);
Eta=zeros(length(I_0),1);
Delta=zeros(length(I_0),1);

%Inversion calculations 

for i=1:length(I_0(:))
        M = K_inv*[I_0(i) I_90(i) I_45(i) I_135(i)]';

        A2=M(1)+M(2)+M(3);

        Pxy=(M(1)-M(2))/A2;
        Puv=2*M(4)/A2;
        Pz=M(3)/A2;

        rho=0.5*atan2(Puv,Pxy);

        lambda_3=Pz+sqrt(Puv^2+Pxy^2);

        delta=2*acos(0.5*abs(-1+sqrt(12*lambda_3-3)));

	if imag(delta)~=0 
      		delta=NaN;
  	end
 
        eta=asin(sqrt(2*Puv/sin(2*rho)/(3*lambda_3-1)));
        
	if imag(eta)~=0 
	      eta=NaN;
	end

        Rho(i) = radtodeg(rho);
        Eta(i) = radtodeg(eta);
        Delta(i) = radtodeg(delta);
end