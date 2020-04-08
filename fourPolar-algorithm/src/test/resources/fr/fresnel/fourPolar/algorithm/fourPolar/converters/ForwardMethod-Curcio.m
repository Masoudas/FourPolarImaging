clear, close all

 %clear, close all

%% Simulation of the BFP intensity distribution, following the model of paper from
%% Yan et al. "Computational correction of spatially variant optical aberrations 
%% in 3D single-molecule localization microscopy", Opt. Express, 2019

%% Parameters of the objective 

n_2 = 1.33;                  % refractive index before objective
n_1 = 1.515;                % refractive index after objective
n = n_1/n_2;
NA = 1.45;                % numerical aperture
focal_length=3.333333*1E3 ;     % focal length (in um)
lambda=0.520   ;             % wavelength (in um)
K0=2*pi/lambda;
K_3=K0*n_1;
theta_3max = asin(NA/n_1);                 % maximal value of theta_3 (in rad), opening angle of objective

% sampling for k vectors over the numerical aperture of objective
n_theta_3 = 180;                                            % number of theta-values
n_phi_3 = 360;                                              % number of phi-values
delta_theta_3 = theta_3max/n_theta_3;                       % interval between two theta_3-steps
delta_phi_3 = 2*pi/n_phi_3;                                 % interval between two phi_3-steps
theta_3 = 0:delta_theta_3:(theta_3max-delta_theta_3);       % angle created with the optical axis (-Z)
phi_3 = 0:delta_phi_3:(2*pi-delta_phi_3);                   % angle in the plane normal to optical axis

% absorption probability
Proba_abs = ones(n_theta_3,n_phi_3);

%% dipoles and electric field in the (p,s,z) frame
% orientation of the dipole

dtheta = pi/36;          % theta dipole
dphi = pi/36;            % phi dipole
ddelta = pi/180;
theta_vec = 0:dtheta:pi/2;  % Dipole's orientation angles
phi_vec = 0:dphi:pi;
delta_vec = 1e-4:ddelta:pi;
Ntheta = length(theta_vec);
Nphi = length(phi_vec);
Ndelta = length(delta_vec);

I0red = zeros(Nphi, Ntheta, Ndelta); 
I90red = zeros(Nphi, Ntheta, Ndelta); 
I45 = zeros(Nphi, Ntheta, Ndelta);  
I135 = zeros(Nphi, Ntheta, Ndelta); 

cos_2=sqrt(1-(n_1/n_2*sin(theta_3')).^2)*ones(1,n_phi_3);
cos_1=cos(theta_3')*ones(1,n_phi_3);
sin_1=sin(theta_3')*ones(1,n_phi_3);

t_p12 = 2*n_2*cos_2./(n_2*cos_1+n_1*cos_2);             % Fresnel transmission coefficient for p-polar
t_s12 = 2*n_2*cos_2./(n_2*cos_2+n_1*cos_1);             % Fresnel transmission coefficient for s-polar

z_ratio = 0.1;
psy_depth = z_ratio*n_2*2*pi*cos_2;

%Electric fields in the BFP

cos_phi3 = ones(n_theta_3,1)*cos(phi_3);
sin_phi3 = ones(n_theta_3,1)*sin(phi_3);
sin_2phi3 = ones(n_theta_3,1)*sin(2*phi_3);
rho = sin_1;

E_xx = n* ((cos_1./cos_2).*t_s12.*sin_phi3.^2 + t_p12.*cos_phi3.^2.*sqrt(1-rho.^2));
E_xy = -n/2*sin_2phi3.* ((cos_1./cos_2).*t_s12 - t_p12.*sqrt(1-rho.^2));
E_xz = -n^2*(cos_1./cos_2).*t_p12.*rho.*cos_phi3;

E_yx = -n/2*sin_2phi3.* ((cos_1./cos_2).*t_s12 - t_p12.*sqrt(1-rho.^2));
E_yy = n* ((cos_1./cos_2).*t_s12.*cos_phi3.^2 + t_p12.*sin_phi3.^2.*sqrt(1-rho.^2));
E_yz = -n^2*(cos_1./cos_2).*t_p12.*rho.*sin_phi3;

% radiation dipole vector
parfor indx_phi = 1:Nphi
        for indx_theta = 1:Ntheta
            for indx_delta = 1:Ndelta
                phi_d = phi_vec(indx_phi);
                theta_d = theta_vec(indx_theta);
                delta_d = delta_vec(indx_delta);
                    mu3= (((cos(delta_d/2))^3-1)/(3*cos(delta_d/2)-3));
                    mu2 = (1-cos(delta_d/2))*(cos(delta_d/2)+2)/6;
                    mu1 = mu2;

                    P_dipole3_x=-sin(theta_d)*ones(n_theta_3,1)*cos(phi_d*ones(1,n_phi_3)).*sqrt(mu3);
                    P_dipole3_y=-sin(theta_d)*ones(n_theta_3,1)*sin(phi_d*ones(1,n_phi_3)).*sqrt(mu3);
                    P_dipole3_z=cos(theta_d)*ones(n_theta_3,n_phi_3).*sqrt(mu3);

                    P_dipole2_x=-cos(theta_d)*ones(n_theta_3,1)*cos(phi_d*ones(1,n_phi_3)).*sqrt(mu2);
                    P_dipole2_y=-cos(theta_d)*ones(n_theta_3,1)*sin(phi_d*ones(1,n_phi_3)).*sqrt(mu2);
                    P_dipole2_z=-sin(theta_d)*ones(n_theta_3,n_phi_3).*sqrt(mu2);

                    P_dipole1_x=-ones(n_theta_3,1)*sin(phi_d*ones(1,n_phi_3)).*sqrt(mu1);
                    P_dipole1_y=ones(n_theta_3,1)*cos(phi_d*ones(1,n_phi_3)).*sqrt(mu1);
                    P_dipole1_z=zeros(n_theta_3,n_phi_3).*sqrt(mu1);

                    % P_dipoles_x = P_dipole1_x+P_dipole2_x+P_dipole3_x;
                    % P_dipoles_y = P_dipole1_y+P_dipole2_y+P_dipole3_y;
                    % P_dipoles_z = P_dipole1_z+P_dipole2_z+P_dipole3_z;

                    Ex1_BFP = E_xx.*P_dipole1_x + E_xy.*P_dipole1_y + E_xz.*P_dipole1_z;
                    Ey1_BFP = E_yx.*P_dipole1_x + E_yy.*P_dipole1_y + E_yz.*P_dipole1_z;
                    E_45_1BFP = (Ex1_BFP+Ey1_BFP)/sqrt(2);
                    E_135_1BFP = (-Ex1_BFP+Ey1_BFP)/sqrt(2);

                    Ex2_BFP = E_xx.*P_dipole2_x + E_xy.*P_dipole2_y + E_xz.*P_dipole2_z;
                    Ey2_BFP = E_yx.*P_dipole2_x + E_yy.*P_dipole2_y + E_yz.*P_dipole2_z;
                    E_45_2BFP = (Ex2_BFP+Ey2_BFP)/sqrt(2);
                    E_135_2BFP = (-Ex2_BFP+Ey2_BFP)/sqrt(2);

                    Ex3_BFP = E_xx.*P_dipole3_x + E_xy.*P_dipole3_y + E_xz.*P_dipole3_z;
                    Ey3_BFP = E_yx.*P_dipole3_x + E_yy.*P_dipole3_y + E_yz.*P_dipole3_z;
                    E_45_3BFP = (Ex3_BFP+Ey3_BFP)/sqrt(2);
                    E_135_3BFP = (-Ex3_BFP+Ey3_BFP)/sqrt(2);

                    I_0 = abs(Ex1_BFP.*exp(1i*psy_depth)).^2 + abs(Ex2_BFP.*exp(1i*psy_depth)).^2 + abs(Ex3_BFP.*exp(1i*psy_depth)).^2;
                    I_90 = abs(Ey1_BFP.*exp(1i*psy_depth)).^2 + abs(Ey2_BFP.*exp(1i*psy_depth)).^2 + abs(Ey3_BFP.*exp(1i*psy_depth)).^2;
                    I_45 = abs(E_45_1BFP.*exp(1i*psy_depth)).^2 + abs(E_45_2BFP.*exp(1i*psy_depth)).^2 + abs(E_45_3BFP.*exp(1i*psy_depth)).^2;
                    I_135 = abs(E_135_1BFP.*exp(1i*psy_depth)).^2 + abs(E_135_2BFP.*exp(1i*psy_depth)).^2 + abs(E_135_3BFP.*exp(1i*psy_depth)).^2;

                 %% sub dividing the BFP
                    factor = 0.7;
                    n_theta3_max = find(theta_3<asin(factor*sin(theta_3max)), 1, 'last' );
                    sin_theta3 = sin(theta_3)'*ones(1,n_phi_3);

                    I0red(indx_phi, indx_theta, indx_delta) = sum(sum(I_0(1:n_theta3_max,:).*sin_theta3(1:n_theta3_max,:))); 
                    I90red(indx_phi, indx_theta, indx_delta) = sum(sum(I_90(1:n_theta3_max,:).*sin_theta3(1:n_theta3_max,:))); 
                    I45(indx_phi, indx_theta, indx_delta) = sum(sum(I_45.*sin_theta3));  
                    I135(indx_phi, indx_theta, indx_delta) = sum(sum(I_135.*sin_theta3));  

             end
        end
end

NameForFile = 'inverse_YanAxelrod';
MatrixFileName = [NameForFile '-NA_' num2str(NA) '-n_' num2str(n_2) '-epi+z.mat'];  
save(MatrixFileName,'NA','I0red','I90red','I45','I135','phi_vec','theta_vec','delta_vec','z_ratio','n_2', 'factor')
