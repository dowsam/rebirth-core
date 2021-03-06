/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-core SigarNetworkProbe.java 2012-7-6 14:29:22 l.xue.nong$$
 */

package cn.com.rebirth.core.monitor.network;

import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Tcp;

import cn.com.rebirth.commons.component.AbstractComponent;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.Inject;
import cn.com.rebirth.core.monitor.sigar.SigarService;

/**
 * The Class SigarNetworkProbe.
 *
 * @author l.xue.nong
 */
public class SigarNetworkProbe extends AbstractComponent implements NetworkProbe {

	/** The sigar service. */
	private final SigarService sigarService;

	/**
	 * Instantiates a new sigar network probe.
	 *
	 * @param settings the settings
	 * @param sigarService the sigar service
	 */
	@Inject
	public SigarNetworkProbe(Settings settings, SigarService sigarService) {
		super(settings);
		this.sigarService = sigarService;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.network.NetworkProbe#networkInfo()
	 */
	@Override
	public NetworkInfo networkInfo() {
		Sigar sigar = sigarService.sigar();

		NetworkInfo networkInfo = new NetworkInfo();

		try {
			NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(null);
			networkInfo.primary = new NetworkInfo.Interface(netInterfaceConfig.getName(),
					netInterfaceConfig.getAddress(), netInterfaceConfig.getHwaddr());
		} catch (SigarException e) {

		}

		return networkInfo;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.network.NetworkProbe#networkStats()
	 */
	@Override
	public synchronized NetworkStats networkStats() {
		Sigar sigar = sigarService.sigar();

		NetworkStats stats = new NetworkStats();
		stats.timestamp = System.currentTimeMillis();

		try {
			Tcp tcp = sigar.getTcp();
			stats.tcp = new NetworkStats.Tcp();
			stats.tcp.activeOpens = tcp.getActiveOpens();
			stats.tcp.passiveOpens = tcp.getPassiveOpens();
			stats.tcp.attemptFails = tcp.getAttemptFails();
			stats.tcp.estabResets = tcp.getEstabResets();
			stats.tcp.currEstab = tcp.getCurrEstab();
			stats.tcp.inSegs = tcp.getInSegs();
			stats.tcp.outSegs = tcp.getOutSegs();
			stats.tcp.retransSegs = tcp.getRetransSegs();
			stats.tcp.inErrs = tcp.getInErrs();
			stats.tcp.outRsts = tcp.getOutRsts();
		} catch (SigarException e) {

		}

		return stats;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.core.monitor.network.NetworkProbe#ifconfig()
	 */
	@Override
	public String ifconfig() {
		Sigar sigar = sigarService.sigar();
		StringBuilder sb = new StringBuilder();
		try {
			for (String ifname : sigar.getNetInterfaceList()) {
				NetInterfaceConfig ifconfig = null;
				try {
					ifconfig = sigar.getNetInterfaceConfig(ifname);
				} catch (SigarException e) {
					sb.append(ifname + "\t" + "Not Avaialbe [" + e.getMessage() + "]");
					continue;
				}
				long flags = ifconfig.getFlags();

				String hwaddr = "";
				if (!NetFlags.NULL_HWADDR.equals(ifconfig.getHwaddr())) {
					hwaddr = " HWaddr " + ifconfig.getHwaddr();
				}

				if (!ifconfig.getName().equals(ifconfig.getDescription())) {
					sb.append(ifconfig.getDescription()).append('\n');
				}

				sb.append(ifconfig.getName() + "\t" + "Link encap:" + ifconfig.getType() + hwaddr).append('\n');

				String ptp = "";
				if ((flags & NetFlags.IFF_POINTOPOINT) > 0) {
					ptp = "  P-t-P:" + ifconfig.getDestination();
				}

				String bcast = "";
				if ((flags & NetFlags.IFF_BROADCAST) > 0) {
					bcast = "  Bcast:" + ifconfig.getBroadcast();
				}

				sb.append("\t" + "inet addr:" + ifconfig.getAddress() + ptp + bcast + "  Mask:" + ifconfig.getNetmask())
						.append('\n');

				sb.append(
						"\t" + NetFlags.getIfFlagsString(flags) + " MTU:" + ifconfig.getMtu() + "  Metric:"
								+ ifconfig.getMetric()).append('\n');
				try {
					NetInterfaceStat ifstat = sigar.getNetInterfaceStat(ifname);

					sb.append(
							"\t" + "RX packets:" + ifstat.getRxPackets() + " errors:" + ifstat.getRxErrors()
									+ " dropped:" + ifstat.getRxDropped() + " overruns:" + ifstat.getRxOverruns()
									+ " frame:" + ifstat.getRxFrame()).append('\n');

					sb.append(
							"\t" + "TX packets:" + ifstat.getTxPackets() + " errors:" + ifstat.getTxErrors()
									+ " dropped:" + ifstat.getTxDropped() + " overruns:" + ifstat.getTxOverruns()
									+ " carrier:" + ifstat.getTxCarrier()).append('\n');
					sb.append("\t" + "collisions:" + ifstat.getTxCollisions()).append('\n');

					long rxBytes = ifstat.getRxBytes();
					long txBytes = ifstat.getTxBytes();

					sb.append(
							"\t" + "RX bytes:" + rxBytes + " (" + Sigar.formatSize(rxBytes) + ")" + "  " + "TX bytes:"
									+ txBytes + " (" + Sigar.formatSize(txBytes) + ")").append('\n');
				} catch (SigarException e) {
				}
			}
			return sb.toString();
		} catch (SigarException e) {
			return "NA";
		}
	}
}
